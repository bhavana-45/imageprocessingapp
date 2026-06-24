export const state = {
  currentImageId: null,
  originalImageId: null,
  originalFile: null,       // kept for Reset (re-upload)
  overlayImageId: null,
  overlayFile: null,
  isLoading: false,
};

export function setImage(imageId) {
  state.currentImageId = imageId;
}

export function setOriginal(imageId, file) {
  state.originalImageId = imageId;
  state.originalFile = file;
  state.currentImageId = imageId;
}

export function setOverlay(imageId, file) {
  state.overlayImageId = imageId;
  state.overlayFile = file;
}

export function clearOverlay() {
  state.overlayImageId = null;
  state.overlayFile = null;
}

export function setLoading(val) {
  state.isLoading = val;
}

export function hasImage() {
  return !!state.currentImageId;
}
