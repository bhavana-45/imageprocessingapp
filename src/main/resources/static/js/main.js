import {
  uploadImage, applyGrayscale, applyBrightness, applyContrast,
  applyBlur, applySharpen, applyRotate, applyFlip, applyZoom,
  applyRemoveBackground, detectShape, layerImages
} from './api.js';

import { state, setOriginal, setImage, setOverlay, clearOverlay } from './state.js';

import {
  refreshPreview, showPlaceholder, setLoading, toast,
  activateGroup, showOp, setControlsEnabled,
  triggerDownload, showShapeResult, toggleOverlayZone
} from './ui.js';

// ── Helpers ───────────────────────────────────────────────────────────────────

async function withLoading(fn) {
  setLoading(true);
  try {
    await fn();
  } catch (err) {
    toast(err.message || 'Something went wrong', 'error');
  } finally {
    setLoading(false);
  }
}

async function afterOp() {
  await refreshPreview(state.currentImageId);
}

// ── Upload ────────────────────────────────────────────────────────────────────

async function handleUpload(file) {
  if (!file || !file.type.startsWith('image/')) {
    toast('Please upload a valid image file', 'error');
    return;
  }
  await withLoading(async () => {
    const imageId = await uploadImage(file);
    setOriginal(imageId, file);
    await refreshPreview(imageId);
    setControlsEnabled(true);
    toast('Image uploaded');
  });
}

function setupUploadZone(dropZoneId, inputId, handler) {
  const zone = document.getElementById(dropZoneId);
  const input = document.getElementById(inputId);

  zone.addEventListener('click', () => input.click());

  input.addEventListener('change', () => {
    if (input.files[0]) handler(input.files[0]);
  });

  zone.addEventListener('dragover', e => {
    e.preventDefault();
    zone.classList.add('drag-over');
  });

  zone.addEventListener('dragleave', () => zone.classList.remove('drag-over'));

  zone.addEventListener('drop', e => {
    e.preventDefault();
    zone.classList.remove('drag-over');
    const file = e.dataTransfer.files[0];
    if (file) handler(file);
  });
}

// ── Slider ↔ number sync ───────────────────────────────────────────────────────

function syncSlider(sliderId, numberId) {
  const slider = document.getElementById(sliderId);
  const number = document.getElementById(numberId);
  slider.addEventListener('input', () => { number.value = slider.value; });
  number.addEventListener('input', () => {
    const v = Math.min(Math.max(number.value, slider.min), slider.max);
    slider.value = v;
    number.value = v;
  });
}

// ── Operation handlers ────────────────────────────────────────────────────────

async function opGrayscale() {
  await withLoading(async () => {
    await applyGrayscale(state.currentImageId);
    await afterOp();
    toast('Grayscale applied');
  });
}

async function opBrightness() {
  const val = parseInt(document.getElementById('brightness-val').value, 10);
  await withLoading(async () => {
    await applyBrightness(state.currentImageId, val);
    await afterOp();
    toast(`Brightness adjusted by ${val > 0 ? '+' : ''}${val}`);
  });
}

async function opContrast() {
  const val = parseFloat(document.getElementById('contrast-val').value);
  await withLoading(async () => {
    await applyContrast(state.currentImageId, val);
    await afterOp();
    toast(`Contrast set to ${val}×`);
  });
}

async function opBlur() {
  const val = parseInt(document.getElementById('blur-val').value, 10);
  await withLoading(async () => {
    await applyBlur(state.currentImageId, val);
    await afterOp();
    toast(`Blur intensity ${val} applied`);
  });
}

async function opSharpen() {
  const val = parseInt(document.getElementById('sharpen-val').value, 10);
  await withLoading(async () => {
    await applySharpen(state.currentImageId, val);
    await afterOp();
    toast(`Sharpen intensity ${val} applied`);
  });
}

async function opRotate() {
  const angle = parseFloat(document.getElementById('rotate-angle').value);
  await withLoading(async () => {
    await applyRotate(state.currentImageId, angle);
    await afterOp();
    toast(`Rotated ${angle}°`);
  });
}

async function opFlip() {
  const dir = document.querySelector('input[name="flip-dir"]:checked').value;
  await withLoading(async () => {
    await applyFlip(state.currentImageId, dir);
    await afterOp();
    toast(`Flipped ${dir}`);
  });
}

async function opZoomIn() {
  const factor = parseFloat(document.getElementById('zoom-in-factor').value);
  if (factor <= 1) { toast('Zoom In factor must be > 1', 'error'); return; }
  await withLoading(async () => {
    await applyZoom(state.currentImageId, factor);
    await afterOp();
    toast(`Zoomed in ×${factor}`);
  });
}

async function opZoomOut() {
  const factor = parseFloat(document.getElementById('zoom-out-factor').value);
  if (factor <= 0 || factor >= 1) { toast('Zoom Out factor must be between 0 and 1', 'error'); return; }
  await withLoading(async () => {
    await applyZoom(state.currentImageId, factor);
    await afterOp();
    toast(`Zoomed out ×${factor}`);
  });
}

async function opRemoveBg() {
  await withLoading(async () => {
    await applyRemoveBackground(state.currentImageId);
    await afterOp();
    toast('Background removed');
  });
}

async function opDetectShape() {
  await withLoading(async () => {
    const result = await detectShape(state.currentImageId);
    showShapeResult(result);
    toast('Shape detected');
  });
}

async function opLayer() {
  if (!state.overlayImageId) {
    toast('Upload an overlay image first', 'error');
    return;
  }
  const x = parseInt(document.getElementById('layer-x').value, 10) || 0;
  const y = parseInt(document.getElementById('layer-y').value, 10) || 0;
  await withLoading(async () => {
    const newId = await layerImages(state.currentImageId, state.overlayImageId, x, y);
    setImage(newId);
    clearOverlay();
    document.getElementById('overlay-input').value = '';
    await afterOp();
    toast('Images layered successfully');
  });
}

async function opReset() {
  if (!state.originalFile) return;
  await withLoading(async () => {
    const imageId = await uploadImage(state.originalFile);
    setImage(imageId);
    // keep originalFile intact, update originalImageId
    state.originalImageId = imageId;
    await refreshPreview(imageId);
    toast('Image reset to original');
  });
}

// ── Init ──────────────────────────────────────────────────────────────────────

function init() {
  // Upload zones
  setupUploadZone('upload-zone', 'file-input', handleUpload);
  setupUploadZone('overlay-drop', 'overlay-input', async (file) => {
    await withLoading(async () => {
      const id = await uploadImage(file);
      setOverlay(id, file);
      document.getElementById('overlay-label').textContent = `✓ ${file.name}`;
      toast('Overlay image ready');
    });
  });

  // Group tabs
  document.querySelectorAll('.group-tab').forEach(tab => {
    tab.addEventListener('click', () => activateGroup(tab.dataset.target));
  });

  // Op selectors
  document.querySelectorAll('.op-selector').forEach(btn => {
    btn.addEventListener('click', () => {
      document.querySelectorAll('.op-selector').forEach(b => b.classList.remove('active'));
      btn.classList.add('active');
      showOp(btn.dataset.panel);
      if (btn.dataset.panel === 'panel-layer') {
        toggleOverlayZone(true);
      } else {
        toggleOverlayZone(false);
      }
    });
  });

  // Slider syncs
  syncSlider('brightness-slider', 'brightness-val');
  syncSlider('contrast-slider', 'contrast-val');
  syncSlider('blur-slider', 'blur-val');
  syncSlider('sharpen-slider', 'sharpen-val');

  // Apply buttons
  document.getElementById('apply-grayscale').addEventListener('click', opGrayscale);
  document.getElementById('apply-brightness').addEventListener('click', opBrightness);
  document.getElementById('apply-contrast').addEventListener('click', opContrast);
  document.getElementById('apply-blur').addEventListener('click', opBlur);
  document.getElementById('apply-sharpen').addEventListener('click', opSharpen);
  document.getElementById('apply-rotate').addEventListener('click', opRotate);
  document.getElementById('apply-flip').addEventListener('click', opFlip);
  document.getElementById('apply-zoom-in').addEventListener('click', opZoomIn);
  document.getElementById('apply-zoom-out').addEventListener('click', opZoomOut);
  document.getElementById('apply-remove-bg').addEventListener('click', opRemoveBg);
  document.getElementById('apply-detect-shape').addEventListener('click', opDetectShape);
  document.getElementById('apply-layer').addEventListener('click', opLayer);

  // Download & Reset
  document.getElementById('download-btn').addEventListener('click', () => {
    triggerDownload(state.currentImageId);
  });
  document.getElementById('reset-btn').addEventListener('click', opReset);

  // Initial state
  setControlsEnabled(false);
  activateGroup('enhance');
  showOp('panel-brightness');
  document.querySelector('[data-panel="panel-brightness"]').classList.add('active');
}

document.addEventListener('DOMContentLoaded', init);
