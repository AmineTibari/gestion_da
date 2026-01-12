<script setup>
import { ref, computed, watch } from 'vue'

const props = defineProps({
  message: String,
  type: { type: String, default: 'info' }, // success, error, warning, info
  duration: { type: Number, default: 3000 },
  show: Boolean
})

const emit = defineEmits(['close'])

const isVisible = ref(false)
const progress = ref(100)
let timer = null
let progressTimer = null

watch(() => props.show, (newVal) => {
  if (newVal) {
    isVisible.value = true
    progress.value = 100
    startTimer()
  }
})

const startTimer = () => {
  const interval = 10
  const step = (100 / props.duration) * interval
  
  progressTimer = setInterval(() => {
    progress.value -= step
    if (progress.value <= 0) {
      closeToast()
    }
  }, interval)
}

const closeToast = () => {
  clearInterval(progressTimer)
  isVisible.value = false
  setTimeout(() => emit('close'), 300)
}

const icon = computed(() => {
  switch(props.type) {
    case 'success': return '✓'
    case 'error': return '✕'
    case 'warning': return '⚠'
    default: return 'ℹ'
  }
})
</script>

<template>
  <Teleport to="body">
    <Transition name="slide">
      <div v-if="isVisible" :class="['toast', type]">
        <div class="toast-icon">{{ icon }}</div>
        <div class="toast-content">{{ message }}</div>
        <button class="toast-close" @click="closeToast">×</button>
        <div class="toast-progress" :style="{ width: progress + '%' }"></div>
      </div>
    </Transition>
  </Teleport>
</template>

<style scoped>
.toast {
  position: fixed;
  top: 24px;
  right: 24px;
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 320px;
  padding: 16px 20px;
  background: rgba(15, 23, 42, 0.95);
  backdrop-filter: blur(12px);
  border-radius: 12px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
  overflow: hidden;
  z-index: 9999;
}

.toast-icon {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: bold;
  color: white;
}

.toast.success .toast-icon { background: linear-gradient(135deg, #10b981, #059669); }
.toast.error .toast-icon { background: linear-gradient(135deg, #ef4444, #dc2626); }
.toast.warning .toast-icon { background: linear-gradient(135deg, #f59e0b, #d97706); }
.toast.info .toast-icon { background: linear-gradient(135deg, #06b6d4, #0891b2); }

.toast-content {
  flex: 1;
  color: white;
  font-family: 'Inter', sans-serif;
  font-size: 14px;
  font-weight: 500;
}

.toast-close {
  background: none;
  border: none;
  color: rgba(255,255,255,0.5);
  font-size: 20px;
  cursor: pointer;
  padding: 0;
  line-height: 1;
}

.toast-close:hover {
  color: white;
}

.toast-progress {
  position: absolute;
  bottom: 0;
  left: 0;
  height: 3px;
  transition: width 0.1s linear;
}

.toast.success .toast-progress { background: linear-gradient(90deg, #10b981, #34d399); }
.toast.error .toast-progress { background: linear-gradient(90deg, #ef4444, #f87171); }
.toast.warning .toast-progress { background: linear-gradient(90deg, #f59e0b, #fbbf24); }
.toast.info .toast-progress { background: linear-gradient(90deg, #06b6d4, #22d3ee); }

.slide-enter-active { animation: slideIn 0.3s ease; }
.slide-leave-active { animation: slideOut 0.3s ease; }

@keyframes slideIn {
  from { transform: translateX(120%); opacity: 0; }
  to { transform: translateX(0); opacity: 1; }
}

@keyframes slideOut {
  from { transform: translateX(0); opacity: 1; }
  to { transform: translateX(120%); opacity: 0; }
}
</style>
