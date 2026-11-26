/* Buildless, same-origin frontend. The backend remains the authority for identity and permissions. */
'use strict';
const $ = (selector, root = document) => root.querySelector(selector);
const esc = value => String(value ?? '').replace(/[&<>"']/g, ch => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[ch]));
const state = {user: null, route: 0, article: null, editor: null, history: null, authMode: 'login'};
const PAGE_SIZE = 8;
const requests = [];
let toastTimer;

class ApiError extends Error {
  constructor(message, status) { super(message); this.status = status; }
}

function toast(message) {
  clearTimeout(toastTimer);
  $('#toast').textContent = message;
  $('#toast').hidden = false;
  toastTimer = setTimeout(() => { $('#toast').hidden = true; }, 5500);
}

function recordRequest(method, path, status, started) {
  if (path === '/api/auth/csrf') return;
  requests.unshift(`${method} ${path} · ${status} · ${Math.round(performance.now() - started)} ms`);
  requests.splice(8);
  $('#request-log').innerHTML = requests.map(row => `<li>${esc(row)}</li>`).join('');
}

async function api(path, {method = 'GET', body, form = false, authFlow = false} = {}) {
  const headers = {Accept: 'application/json'};
  if (method !== 'GET') {
    // Fetch a current token for each write, including login/logout; never retry a write silently.
    const csrf = await api('/api/auth/csrf');
    headers[csrf.headerName] = csrf.token;
  }
  if (body !== undefined) headers['Content-Type'] = form ? 'application/x-www-form-urlencoded' : 'application/json';
  const started = performance.now();
  let response;
  try {
    response = await fetch(path, {method, headers, credentials: 'same-origin', cache: 'no-store',
      signal: AbortSignal.timeout(15000),
      body: body === undefined ? undefined : form ? new URLSearchParams(body).toString() : JSON.stringify(body)});
  } catch (error) {
    recordRequest(method, path, '连接失败', started);
    throw new ApiError(method === 'GET' ? '无法连接服务，请检查 Java 项目是否已启动，再重试。' :
      '连接中断，操作结果尚未确认。请先刷新核对，避免重复提交。', 0);
  }
  recordRequest(method, path, response.status, started);
  const finalUrl = new URL(response.url);
  if (path === '/login' && finalUrl.searchParams.has('error')) throw new ApiError('用户名或密码不正确，请重新输入。', 401);
  if (authFlow && path === '/logout' && response.ok) return null;
  const isJson = (response.headers.get('content-type') || '').includes('json');
  if (response.redirected && finalUrl.pathname === '/login' || response.status === 401) {
    state.user = null;
    renderAccount();
    throw new ApiError('登录状态已失效，请重新登录后再操作。', 401);
  }
  const data = isJson ? await response.json() : null;
  if (!response.ok) {
    const fallback = {403: '请求被拒绝。请检查操作权限；会话过期时重新登录后再试。',
      404: '内容不存在，可能已被删除。', 409: '内容已更新，请重新读取后操作。',
      429: '评论太频繁了，请稍后再试。', 503: '服务暂时不可用，请稍后刷新。'};
    throw new ApiError(data?.detail || fallback[response.status] || `请求失败（${response.status}），请稍后重试。`, response.status);
  }
  if (response.status === 204) return null;
  if (!isJson) throw new ApiError('服务返回了意外的页面，请刷新后重试。', response.status);
  return data;
}

function errorBox(error) { return `<div class="error-box" role="alert">${esc(error.message)}</div>`; }
function empty(title, description, action = '') {
  return `<div class="empty"><div class="empty-symbol" aria-hidden="true">{ }</div><h3>${esc(title)}</h3><p>${esc(description)}</p>${action}</div>`;
}
function showFormError(id, error) {
  const target = $(id);
  if (!target) { toast(error.message); return; }
  target.textContent = error.message;
  target.hidden = false;
}
async function busy(button, action, errorTarget) {
  if (button.disabled) return;
  button.disabled = true;
  try { await action(); } catch (error) { errorTarget ? showFormError(errorTarget, error) : toast(error.message); }
  finally { button.disabled = false; }
}
function renderAccount() {
  $('#account').innerHTML = state.user ? `<span class="avatar">${esc(state.user.username.slice(0, 1).toUpperCase())}</span><span class="account-name" title="${esc(state.user.username)}">${esc(state.user.username)}</span><button id="logout" class="text-button">退出</button>` : '<button id="login" class="text-button">登录 / 注册</button>';
  $('#login')?.addEventListener('click', () => openAuth());
  $('#logout')?.addEventListener('click', event => busy(event.currentTarget, async () => {
    await api('/logout', {method: 'POST', authFlow: true});
    state.user = null;
    renderAccount();
    toast('已退出登录');
    await renderRoute();
  }));
}
async function refreshUser() {
  try { state.user = await api('/api/auth/me'); }
  catch (error) { if (error.status !== 401) throw error; state.user = null; }
  renderAccount();
}
function openAuth(mode = 'login') {
  state.authMode = mode;
  $('#auth-title').textContent = mode === 'login' ? '欢迎回来' : '加入拾光社区';
  $('#auth-description').textContent = mode === 'login' ? '登录后，分享你的想法与实践。' : '创建一个账号，开始记录与交流。';
  $('#auth-submit').textContent = mode === 'login' ? '登录' : '创建账号';
  $('#auth-switch').textContent = mode === 'login' ? '还没有账号？注册一个' : '已有账号？去登录';
  const password = $('#auth-form').elements.password;
  password.autocomplete = mode === 'login' ? 'current-password' : 'new-password';
  password.minLength = mode === 'login' ? 1 : 8;
  password.maxLength = mode === 'login' ? 256 : 72;
  password.placeholder = mode === 'login' ? '请输入密码' : '8–72 个字符，UTF-8 编码不超过 72 字节';
  $('#auth-error').hidden = true;
  if (!$('#auth-dialog').open) $('#auth-dialog').showModal();
}
function requireUser() { if (state.user) return true; openAuth(); return false; }
$('#auth-switch').addEventListener('click', () => openAuth(state.authMode === 'login' ? 'register' : 'login'));
$('#auth-form').addEventListener('submit', event => {
  event.preventDefault();
  busy($('#auth-submit'), async () => {
    $('#auth-error').hidden = true;
    const body = Object.fromEntries(new FormData(event.target));
    if (state.authMode === 'register') {
      if (new TextEncoder().encode(body.password).length > 72) throw new ApiError('密码的 UTF-8 长度不能超过 72 字节。', 400);
      await api('/api/auth/register', {method: 'POST', body});
      openAuth('login');
      toast('注册成功，请使用新账号登录');
      return;
    }
    await api('/login', {method: 'POST', body, form: true, authFlow: true});
    await refreshUser();
    $('#auth-dialog').close();
    event.target.elements.password.value = '';
    toast('登录成功，欢迎回来');
    await renderRoute();
  }, '#auth-error');
});

function pageRoute() {
  const [path, query] = (location.hash.slice(1) || 'feed').split('?');
  const params = new URLSearchParams(query);
  const page = Math.min(2147483647, Math.max(1, Number.parseInt(params.get('page'), 10) || 1));
  return {path, page, keyword: params.get('keyword') || ''};
}
function navigate(hash) { if (location.hash === '#' + hash) renderRoute(); else location.hash = hash; }
function listHash(path, page, keyword = '') {
  const params = new URLSearchParams({page});
  if (keyword) params.set('keyword', keyword);
  return `${path}?${params}`;
}
function pager(page, count, prefix = 'list') {
  return `<div class="pager"><button id="${prefix}-prev" ${page <= 1 ? 'disabled' : ''}>上一页</button><span>第 ${page} 页</span><button id="${prefix}-next" ${count < PAGE_SIZE ? 'disabled' : ''}>下一页</button></div>`;
}
function bindPager(prefix, page, handler) {
  $(`#${prefix}-prev`)?.addEventListener('click', () => handler(page - 1));
  $(`#${prefix}-next`)?.addEventListener('click', () => handler(page + 1));
}
function articleCard(article) {
  return `<a class="article-card" href="#article/${esc(article.id)}"><div class="article-meta"><span class="avatar">${esc(String(article.authorId ?? '?').slice(-2))}</span><span>作者 #${esc(article.authorId)}</span><span>·</span><span>文章 #${esc(article.id)}</span></div><h3>${esc(article.title)}</h3><p class="excerpt">${esc(article.content)}</p><div class="card-bottom"><span class="pill">社区分享</span><span class="read-label">阅读文章</span></div></a>`;
}

async function renderRoute() {
  const epoch = ++state.route;
  state.article = null;
  const route = pageRoute();
  document.querySelectorAll('[data-nav]').forEach(link => {
    const active = link.dataset.nav === route.path;
    link.classList.toggle('active', active);
    active ? link.setAttribute('aria-current', 'page') : link.removeAttribute('aria-current');
  });
  $('#main').setAttribute('aria-busy', 'true');
  window.scrollTo(0, 0);
  $('#main').innerHTML = '<div class="loading" role="status">正在加载社区内容…</div>';
  $('#search-input').value = route.keyword;
  try {
    if (/^article\/\d+$/.test(route.path)) await renderArticle(route.path.split('/')[1], epoch);
    else if (route.path === 'hot') await renderHotPage(epoch);
    else if (['feed', 'favorites'].includes(route.path)) await renderList(route, epoch);
    else if (epoch === state.route) $('#main').innerHTML = empty('页面不存在', '请从左侧导航返回社区。', '<a class="text-button" href="#feed">发现文章</a>');
  } catch (error) {
    if (epoch !== state.route) return;
    $('#main').innerHTML = `${errorBox(error)}<div class="pager"><button id="retry">重新加载</button>${error.status === 401 ? '<button id="page-login" class="primary">登录账号</button>' : ''}</div>`;
    $('#retry').onclick = renderRoute;
    $('#page-login')?.addEventListener('click', () => openAuth());
  } finally { if (epoch === state.route) $('#main').removeAttribute('aria-busy'); }
}

async function renderList(route, epoch) {
  const favorites = route.path === 'favorites';
  if (favorites && !state.user) {
    $('#main').innerHTML = empty('收藏你的下一次灵感', '登录后，可以查看和管理自己收藏的文章。', '<button id="page-login" class="primary">登录账号</button>');
    $('#page-login').onclick = () => openAuth();
    return;
  }
  const query = new URLSearchParams({page: route.page, pageSize: PAGE_SIZE});
  if (!favorites && route.keyword) query.set('keyword', route.keyword);
  const articles = await api((favorites ? '/api/me/favorites' : '/api/articles') + '?' + query);
  if (epoch !== state.route) return;
  const title = favorites ? '值得再读的好内容' : route.keyword ? '找到你的下一份灵感' : '在这里，让想法生根。';
  const description = favorites ? '把值得回顾的实践与思考，留在自己的收藏夹。' : '分享开发经验，记录学习日常。每一点积累，都在让你走得更远。';
  document.title = `${favorites ? '我的收藏' : '发现文章'} · 拾光社区`;
  $('#main').innerHTML = `<section class="welcome"><div><span class="eyebrow">${favorites ? 'YOUR COLLECTION' : 'A PLACE TO LEARN & SHARE'}</span><h1>${title}</h1><p>${description}</p></div><div class="welcome-art" aria-hidden="true"><span class="code-tile">&lt;/&gt;</span></div></section><div class="content-grid"><section><div class="section-head"><div class="section-label"><h2>${favorites ? '我的收藏' : route.keyword ? '搜索结果' : '最新文章'}</h2><span class="pill">${articles.length} 篇 · 本页</span></div><span class="muted">${esc(route.keyword ? `标题包含「${route.keyword}」` : favorites ? '按收藏时间排序' : '发现社区里的新声音')}</span></div>${articles.length ? `<div class="article-list">${articles.map(articleCard).join('')}</div>` : empty(route.page > 1 ? '这一页没有更多内容了' : favorites ? '收藏夹还是空的' : route.keyword ? '暂时没有匹配的文章' : '故事，从第一篇文章开始', route.page > 1 ? '可以返回上一页继续浏览。' : favorites ? '打开一篇喜欢的文章，点击收藏即可。' : route.keyword ? '换一个标题关键词再试试。' : '点击右上角“写文章”，分享你的第一次实践。')}${pager(route.page, articles.length)}</section><aside class="rail"><section class="rail-card"><div class="section-head"><h3>热门文章</h3><a class="text-button" href="#hot">全部</a></div><div id="hot-list" class="muted">正在获取榜单…</div></section><section class="rail-card guide-card"><span class="eyebrow">HELLO, DEVELOPER</span><h3 style="margin-top:12px">把实践变成自己的积累</h3><p>读一篇文章，留一个想法。<br>也欢迎分享你刚刚解决的问题。</p><div class="guide-tags"><span>Java</span><span>Spring Boot</span><span>MySQL</span><span>Redis</span></div></section></aside></div>`;
  bindPager('list', route.page, page => navigate(listHash(route.path, page, route.keyword)));
  await fillHot($('#hot-list'), 5, epoch);
}
async function fillHot(container, limit, epoch) {
  try {
    const ranking = await api('/api/articles/hot?limit=' + limit);
    // The ranking API exposes IDs/counts only. Load titles without inventing article metadata.
    const entries = await Promise.all(ranking.map(async (row) => {
      try { return {...row, article: await api('/api/articles/' + row.articleId)}; }
      catch (error) { return {...row, unavailable: error.status === 404, titleError: error.status !== 404}; }
    }));
    if (epoch !== state.route || !container.isConnected) return;
    container.innerHTML = entries.length ? entries.map((row, index) => `<div class="hot-item"><span class="hot-rank">${String(index + 1).padStart(2, '0')}</span><div>${row.unavailable ? '<span class="hot-name muted">文章已删除，等待榜单更新</span>' : `<a class="hot-name" href="#article/${esc(row.articleId)}">${esc(row.article?.title || `文章 #${row.articleId}`)}</a>`}<span class="hot-count">${esc(row.likeCount)} 次点赞${row.titleError ? ' · 标题暂时无法加载' : ''}</span></div></div>`).join('') : '<p class="muted">榜单还在积累中，稍后再来看看。</p>';
  } catch (error) { if (epoch === state.route && container.isConnected) container.innerHTML = errorBox(error); }
}
async function renderHotPage(epoch) {
  document.title = '热门排行 · 拾光社区';
  $('#main').innerHTML = '<div class="page-heading"><span class="eyebrow">COMMUNITY HIGHLIGHTS</span><h1>好内容，值得被看见。</h1><p class="muted">按点赞数排列 · 榜单约每分钟更新，点赞后可能稍有延迟。</p></div><div class="section-head"><h2>热门排行 · Top 10</h2><button id="refresh-hot">刷新榜单</button></div><div class="ranking-list" id="ranking"><div class="loading">正在获取榜单…</div></div>';
  $('#refresh-hot').onclick = event => busy(event.currentTarget, () => fillHot($('#ranking'), 10, epoch));
  await fillHot($('#ranking'), 10, epoch);
}

async function renderArticle(id, epoch) {
  const article = await api('/api/articles/' + id);
  if (epoch !== state.route) return;
  state.article = article;
  const owner = state.user && String(state.user.id) === String(article.authorId);
  document.title = `${article.title} · 拾光社区`;
  $('#main').innerHTML = `<div class="article-page"><a class="back-link" href="#feed">返回文章列表</a><article class="paper"><div class="article-meta"><span class="pill">社区分享</span><span>作者 #${esc(article.authorId)}</span><span>· 文章 #${esc(article.id)}</span><span>· 当前版本 ${esc(article.version)}</span></div><h1>${esc(article.title)}</h1><div class="article-content">${esc(article.content)}</div><div class="article-toolbar"><button id="like-button" ${state.user ? 'disabled' : ''}>♡ 点赞</button><button id="favorite-button">☆ 收藏</button><button id="unfavorite-button" title="取消收藏是幂等操作">取消收藏</button>${owner ? '<div class="author-toolbar"><button id="edit-button">编辑</button><button id="history-button">历史版本</button><button id="delete-button" class="danger">删除</button></div>' : ''}</div><p class="muted" style="margin:14px 0 0">${state.user ? '收藏后可在“我的收藏”中查看；重复收藏不会新增记录。' : '登录后参与点赞、收藏与讨论。'}</p></article><section class="paper comments"><div class="section-head"><h2>一起聊聊</h2><span class="muted">每一个想法，都值得交流</span></div><div id="comments-list"></div><form id="comment-form" class="comment-form"><label for="comment-content">发表评论</label><textarea id="comment-content" name="content" required maxlength="1000" rows="3" placeholder="说说你的想法…"></textarea><p id="comment-error" class="form-error" role="alert" hidden></p><div class="editor-bottom"><span class="muted">最多 1000 字 · 每分钟最多 5 次请求</span><button class="primary" id="comment-submit" type="submit">发布评论</button></div></form></section></div>`;
  $('#favorite-button').onclick = event => { if (requireUser()) busy(event.currentTarget, async () => { await api(`/api/articles/${id}/favorite`, {method:'PUT'}); toast('已收藏，可在“我的收藏”中查看'); }); };
  $('#unfavorite-button').onclick = event => { if (requireUser()) busy(event.currentTarget, async () => { await api(`/api/articles/${id}/favorite`, {method:'DELETE'}); toast('已取消收藏'); }); };
  $('#edit-button')?.addEventListener('click', () => openEditor(article));
  $('#history-button')?.addEventListener('click', () => openHistory(article));
  $('#delete-button')?.addEventListener('click', event => {
    if (!confirm(`确定删除《${article.title}》？删除后无法从页面恢复。`)) return;
    busy(event.currentTarget, async () => { await api('/api/articles/' + id, {method:'DELETE'}); toast('文章已删除'); navigate('feed'); });
  });
  let liked = null;
  const likeButton = $('#like-button');
  likeButton.onclick = event => {
    if (!requireUser()) return;
    busy(event.currentTarget, async () => {
      if (liked === null) { liked = await api(`/api/articles/${id}/like`); }
      else { await api(`/api/articles/${id}/like`, {method: liked ? 'DELETE' : 'PUT'}); liked = !liked; toast(liked ? '点赞成功' : '已取消点赞'); }
      likeButton.textContent = liked ? '♥ 已点赞' : '♡ 点赞';
      likeButton.classList.toggle('selected', liked);
      likeButton.setAttribute('aria-pressed', String(liked));
    });
  };
  const likeLoad = state.user ? api(`/api/articles/${id}/like`).then(value => {
    if (epoch !== state.route) return;
    liked = value;
    likeButton.textContent = liked ? '♥ 已点赞' : '♡ 点赞';
    likeButton.classList.toggle('selected', liked);
    likeButton.setAttribute('aria-pressed', String(liked));
  }).catch(() => { if (epoch === state.route) likeButton.textContent = '重试点赞状态'; })
    .finally(() => { if (epoch === state.route) likeButton.disabled = false; }) : Promise.resolve();
  let commentRequest = 0;
  async function loadComments(page) {
    const request = ++commentRequest;
    const container = $('#comments-list');
    container.innerHTML = '<div class="loading">正在加载评论…</div>';
    try {
      const comments = await api(`/api/articles/${id}/comments?page=${page}&pageSize=${PAGE_SIZE}`);
      if (epoch !== state.route || request !== commentRequest) return;
      container.innerHTML = comments.length ? comments.map(comment => `<div class="comment-item"><div class="article-meta"><span class="avatar">${esc(String(comment.userId).slice(-2))}</span><span>用户 #${esc(comment.userId)}</span><span>· ${esc((comment.createdAt || '').replace('T',' '))}</span></div><p>${esc(comment.content)}</p></div>`).join('') : '<p class="muted">这一页还没有评论，分享你的想法吧。</p>';
      container.insertAdjacentHTML('beforeend', pager(page, comments.length, 'comments'));
      bindPager('comments', page, loadComments);
    } catch (error) { if (epoch === state.route && request === commentRequest) container.innerHTML = errorBox(error); }
  }
  $('#comment-form').onsubmit = event => {
    event.preventDefault();
    if (!requireUser()) return;
    const form = event.target;
    busy($('#comment-submit'), async () => {
      $('#comment-error').hidden = true;
      const content = form.elements.content.value;
      if (!content.trim()) throw new ApiError('评论不能只包含空白。', 400);
      await api(`/api/articles/${id}/comments`, {method:'POST', body:{content}});
      form.reset();
      toast('评论已发布');
      if (epoch === state.route) await loadComments(1);
    }, '#comment-error');
  };
  await Promise.all([likeLoad, loadComments(1)]);
}

function openEditor(article = null) {
  if (!requireUser()) return;
  // Preserve the version observed when editing begins, including across conflicts.
  state.editor = article ? {id:article.id, version:article.version} : null;
  const form = $('#editor-form');
  form.elements.title.value = article?.title || '';
  form.elements.content.value = article?.content || '';
  $('#editor-title').textContent = article ? '编辑文章' : '写一篇新文章';
  $('#editor-hint').textContent = article ? `基于当前版本 ${article.version} 编辑；保存时会保留修改前的历史快照。` : '记录一次实践，让知识留下来。';
  $('#editor-submit').textContent = article ? '保存修改' : '发布文章';
  $('#editor-error').hidden = true;
  $('#editor-dialog').showModal();
}
$('#write-button').onclick = () => openEditor();
$('#editor-form').onsubmit = event => {
  event.preventDefault();
  busy($('#editor-submit'), async () => {
    $('#editor-error').hidden = true;
    const body = Object.fromEntries(new FormData(event.target));
    if (!body.title.trim() || !body.content.trim()) throw new ApiError('标题和正文不能只包含空白。',400);
    const editing = state.editor;
    if (editing) body.expectedVersion = editing.version;
    let article;
    try { article = await api('/api/articles' + (editing ? '/' + editing.id : ''), {method: editing ? 'PUT' : 'POST', body}); }
    catch (error) {
      if (error.status === 409) error.message += '\n你的输入已保留。请先复制草稿，关闭编辑窗口并重新加载文章，核对最新内容后再编辑。';
      throw error;
    }
    $('#editor-dialog').close();
    toast(editing ? '修改已保存，旧内容已存入历史版本' : '文章发布成功');
    navigate('article/' + article.id);
  }, '#editor-error');
};

async function openHistory(article) {
  state.history = {articleId: article.id, expectedVersion: article.version, request: 0};
  $('#version-dialog').showModal();
  await loadHistory(1);
}
async function loadHistory(page) {
  const history = state.history;
  const request = ++history.request;
  const container = $('#version-body');
  container.innerHTML = '<div class="loading">正在加载历史版本…</div>';
  try {
    const versions = await api(`/api/articles/${history.articleId}/versions?page=${page}&pageSize=${PAGE_SIZE}`);
    if (history !== state.history || request !== history.request) return;
    container.innerHTML = `<p class="muted">历史记录是修改前的快照。恢复会生成一次新编辑，并保留恢复前的内容。当前文章版本：${esc(history.expectedVersion)}。</p>${versions.length ? `<div class="version-list">${versions.map(version => `<details class="version-item"><summary>快照 #${esc(version.id)} · ${esc(version.title)}<span class="hot-count">${esc((version.createdAt || '').replace('T',' '))}</span></summary><div class="snapshot">${esc(version.content)}</div><button data-restore="${esc(version.id)}">恢复此快照</button></details>`).join('')}</div>` : empty('这一页还没有历史快照', '文章首次编辑成功后，修改前的内容会保存在这里。')}${pager(page, versions.length,'versions')}${versions.length ? '<div class="compare-fields"><label>原快照 ID<input id="diff-from" type="number" min="1" step="1" aria-label="原快照 ID"></label><label>目标快照 ID<input id="diff-to" type="number" min="1" step="1" aria-label="目标快照 ID"></label><button id="compare-button">对比快照</button></div><p class="muted">可输入不同页中的快照 ID；同一行位置比较，展示不同的行。</p><div id="diff-result"></div>' : ''}`;
    bindPager('versions', page, loadHistory);
    if (versions.length) {
      $('#diff-from').value = versions[Math.min(1, versions.length - 1)].id;
      $('#diff-to').value = versions[0].id;
      $('#compare-button').onclick = event => busy(event.currentTarget, async () => {
        const from = $('#diff-from').value, to = $('#diff-to').value;
        if (!/^[1-9]\d*$/.test(from) || !/^[1-9]\d*$/.test(to)) throw new ApiError('请输入有效的快照 ID。',400);
        const result = await api(`/api/articles/${history.articleId}/versions/diff?fromVersionId=${from}&toVersionId=${to}`);
        if (history !== state.history || request !== history.request) return;
        $('#diff-result').innerHTML = `<h3>快照 #${esc(from)} 与 #${esc(to)}</h3><p class="muted">左侧为原快照，右侧为目标快照（窄屏上下排列）。</p>${result.titleChanged ? diffRow('标题',result.fromTitle,result.toTitle) : '<p class="muted">标题未变更</p>'}${result.contentChanges.length ? result.contentChanges.map(line => diffRow(`第 ${line.lineNumber} 行`,line.fromText,line.toText)).join('') : '<p class="muted">正文没有差异</p>'}`;
      });
    }
    container.querySelectorAll('[data-restore]').forEach(button => button.addEventListener('click', () => {
      if (!confirm(`确定将文章恢复为快照 #${button.dataset.restore} 的内容？恢复前的内容仍会保存为历史快照。`)) return;
      busy(button, async () => {
        try { await api(`/api/articles/${history.articleId}/versions/${button.dataset.restore}/restore`, {method:'POST', body:{expectedVersion:history.expectedVersion}}); }
        catch (error) {
          if (error.status === 409) error.message += ' 请关闭历史窗口并刷新文章，核对最新内容后再恢复。';
          throw error;
        }
        $('#version-dialog').close();
        toast('恢复成功，文章已生成新版本');
        await renderRoute();
      });
    }));
  } catch (error) { if (history === state.history && request === history.request) container.innerHTML = errorBox(error); }
}
function diffRow(label, from, to) {
  return `<div class="diff-row"><span>${esc(label)}</span><div class="diff-columns"><pre>${esc(from === null ? '（此行不存在）' : from)}</pre><pre>${esc(to === null ? '（此行不存在）' : to)}</pre></div></div>`;
}

document.querySelectorAll('[data-close]').forEach(button => button.onclick = () => button.closest('dialog').close());
// Closing a form during submission could hide an uncertain write result or reset the edit target.
document.querySelectorAll('dialog').forEach(dialog => {
  const pending = () => dialog.querySelector('button[type="submit"]:disabled, button[data-restore]:disabled');
  dialog.addEventListener('cancel', event => { if (pending()) event.preventDefault(); });
  dialog.querySelector('[data-close]').onclick = () => { if (!pending()) dialog.close(); };
});
$('#auth-dialog').addEventListener('close', () => { $('#auth-form').elements.password.value = ''; });
$('#search-form').onsubmit = event => { event.preventDefault(); navigate(listHash('feed',1,$('#search-input').value.trim())); };
$('.skip-link').onclick = event => { event.preventDefault(); $('#main').focus(); };
window.addEventListener('hashchange', renderRoute);
async function start() {
  renderAccount();
  try { await refreshUser(); } catch (error) { toast(error.message); }
  await renderRoute();
}
start();
