const BASE = '/api/images';

async function handleResponse(res) {
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text || `HTTP ${res.status}`);
  }
  return res;
}

export async function uploadImage(file) {
  const form = new FormData();
  form.append('file', file);
  const res = await fetch(`${BASE}/upload`, { method: 'POST', body: form });
  await handleResponse(res);
  const text = await res.text();
  // Response: "Image uploaded successfully.\nImage ID: <uuid>"
  const match = text.match(/Image ID:\s*(\S+)/);
  if (!match) throw new Error('Could not parse image ID from response');
  return match[1];
}

export async function getImageUrl(imageId) {
  return `${BASE}/${imageId}?t=${Date.now()}`;
}

export async function applyGrayscale(imageId) {
  const res = await fetch(`${BASE}/${imageId}/grayscale`, { method: 'POST' });
  return handleResponse(res);
}

export async function applyBrightness(imageId, value) {
  const res = await fetch(`${BASE}/${imageId}/brightness?value=${value}`, { method: 'POST' });
  return handleResponse(res);
}

export async function applyContrast(imageId, factor) {
  const res = await fetch(`${BASE}/${imageId}/contrast?factor=${factor}`, { method: 'POST' });
  return handleResponse(res);
}

export async function applyBlur(imageId, intensity) {
  const res = await fetch(`${BASE}/${imageId}/blur?intensity=${intensity}`, { method: 'POST' });
  return handleResponse(res);
}

export async function applySharpen(imageId, intensity) {
  const res = await fetch(`${BASE}/${imageId}/sharpen?intensity=${intensity}`, { method: 'POST' });
  return handleResponse(res);
}

export async function applyRotate(imageId, angle) {
  const res = await fetch(`${BASE}/${imageId}/rotate?angle=${angle}`, { method: 'POST' });
  return handleResponse(res);
}

export async function applyFlip(imageId, direction) {
  const res = await fetch(`${BASE}/${imageId}/flip?direction=${direction}`, { method: 'POST' });
  return handleResponse(res);
}

export async function applyZoom(imageId, factor) {
  const res = await fetch(`${BASE}/${imageId}/zoom?factor=${factor}`, { method: 'POST' });
  return handleResponse(res);
}

export async function applyRemoveBackground(imageId) {
  const res = await fetch(`${BASE}/${imageId}/remove-background`, { method: 'POST' });
  return handleResponse(res);
}

export async function detectShape(imageId) {
  const res = await fetch(`${BASE}/${imageId}/shape`);
  await handleResponse(res);
  return res.text();
}

export async function layerImages(baseImageId, overlayImageId, x, y) {
  const params = new URLSearchParams({ baseImageId, overlayImageId, x, y });
  const res = await fetch(`${BASE}/layer?${params}`, { method: 'POST' });
  await handleResponse(res);
  const text = await res.text();
  const match = text.match(/New Image ID:\s*(\S+)/);
  if (!match) throw new Error('Could not parse new image ID');
  return match[1];
}
