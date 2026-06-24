import { getImageUrl } from './api.js';

// ── Preview ──────────────────────────────────────────────────────────────────

export async function refreshPreview(imageId) {
  const img = document.getElementById('preview-img');
  const placeholder = document.getElementById('preview-placeholder');

  const url = await getImageUrl(imageId);

  return new Promise((resolve, reject) => {
    img.onload = () => {
      placeholder.style.display = 'none';
      img.style.display = 'block';

      // Update dimension badge
      document.getElementById('dim-w').textContent = img.naturalWidth;
      document.getElementById('dim-h').textContent = img.naturalHeight;
      document.getElementById('image-meta').style.display = 'flex';

      resolve();
    };
    img.onerror = () => reject(new Error('Failed to load image'));
    img.src = url;
  });
}

export function showPlaceholder() {
  document.getElementById('preview-img').style.display = 'none';
  document.getElementById('preview-placeholder').style.display = 'flex';
  document.getElementById('image-meta').style.display = 'none';
}

// ── Shape result ─────────────────────────────────────────────────────────────

export function showShapeResult(text) {
  const el = document.getElementById('shape-result');
  el.textContent = text;
  el.style.display = 'block';
}

export function hideShapeResult() {
  document.getElementById('shape-result').style.display = 'none';
}

// ── Spinner / loading ─────────────────────────────────────────────────────────

export function setLoading(on) {
  document.getElementById('spinner-overlay').style.display = on ? 'flex' : 'none';
  document.querySelectorAll('.apply-btn').forEach(b => b.disabled = on);
}

// ── Toast ─────────────────────────────────────────────────────────────────────

let toastTimer;
export function toast(message, type = 'success') {
  const el = document.getElementById('toast');
  el.textContent = message;
  el.className = `toast toast--${type} toast--visible`;
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => {
    el.classList.remove('toast--visible');
  }, 3200);
}

// ── Panel switching ───────────────────────────────────────────────────────────

export function activateGroup(groupId) {
  document.querySelectorAll('.op-group').forEach(g => {
    g.classList.toggle('op-group--active', g.dataset.group === groupId);
  });
  document.querySelectorAll('.group-tab').forEach(t => {
    t.classList.toggle('group-tab--active', t.dataset.target === groupId);
  });
  hideShapeResult();
}

// ── Operation panels ──────────────────────────────────────────────────────────

export function showOp(opId) {
  document.querySelectorAll('.op-panel').forEach(p => {
    p.classList.toggle('op-panel--active', p.id === opId);
  });
}

// ── Controls enabled/disabled based on image presence ────────────────────────

export function setControlsEnabled(enabled) {
  document.querySelectorAll('.requires-image').forEach(el => {
    el.disabled = !enabled;
    el.classList.toggle('disabled', !enabled);
  });
  document.getElementById('download-btn').style.display = enabled ? 'inline-flex' : 'none';
  document.getElementById('reset-btn').style.display = enabled ? 'inline-flex' : 'none';
}

// ── Download ──────────────────────────────────────────────────────────────────

export function triggerDownload(imageId) {
  const a = document.createElement('a');
  a.href = `/api/images/${imageId}`;
  a.download = `processed-${Date.now()}.png`;
  a.click();
}

// ── Overlay upload zone visibility ────────────────────────────────────────────

export function toggleOverlayZone(show) {
  document.getElementById('overlay-zone').style.display = show ? 'block' : 'none';
}
