<script setup>
import { watch } from 'vue'

const props = defineProps({
  show: Boolean,
  title: String,
  size: { type: String, default: 'md' } // sm, md, lg
})

const emit = defineEmits(['close'])

const closeModal = () => {
  emit('close')
}

// Close on ESC key
watch(() => props.show, (newVal) => {
  if (newVal) {
    document.addEventListener('keydown', handleEsc)
    document.body.style.overflow = 'hidden'
  } else {
    document.removeEventListener('keydown', handleEsc)
    document.body.style.overflow = ''
  }
})

const handleEsc = (e) => {
  if (e.key === 'Escape') closeModal()
}
</script>

<template>
  <Teleport to="body">
    <Transition name="modal">
      <div v-if="show" class="modal-overlay" @click="closeModal">
        <div :class="['modal-container', size]" @click.stop>
          <div class="modal-header" v-if="title">
            <h3>{{ title }}</h3>
            <button class="modal-close" @click="closeModal">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M18 6L6 18M6 6l12 12"/>
              </svg>
            </button>
          </div>
          <div class="modal-body">
            <slot></slot>
          </div>
          <div class="modal-footer" v-if="$slots.footer">
            <slot name="footer"></slot>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<style scoped>
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 24px;
}

.modal-container {
  background: linear-gradient(145deg, rgba(30, 41, 59, 0.98), rgba(15, 23, 42, 0.98));
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 20px;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.5);
  max-height: 90vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.modal-container.sm { width: 100%; max-width: 400px; }
.modal-container.md { width: 100%; max-width: 560px; }
.modal-container.lg { width: 100%; max-width: 800px; }

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px 28px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.modal-header h3 {
  margin: 0;
  color: white;
  font-family: 'Inter', sans-serif;
  font-size: 20px;
  font-weight: 600;
}

.modal-close {
  background: rgba(255, 255, 255, 0.05);
  border: none;
  width: 36px;
  height: 36px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: rgba(255, 255, 255, 0.5);
  transition: all 0.2s;
}

.modal-close:hover {
  background: rgba(255, 255, 255, 0.1);
  color: white;
}

.modal-body {
  padding: 28px;
  overflow-y: auto;
  color: rgba(255, 255, 255, 0.8);
}

.modal-footer {
  padding: 20px 28px;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

/* Animations */
.modal-enter-active { animation: modalIn 0.3s ease; }
.modal-leave-active { animation: modalOut 0.2s ease; }

.modal-enter-active .modal-container { animation: containerIn 0.3s ease; }
.modal-leave-active .modal-container { animation: containerOut 0.2s ease; }

@keyframes modalIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes modalOut {
  from { opacity: 1; }
  to { opacity: 0; }
}

@keyframes containerIn {
  from { transform: scale(0.95) translateY(20px); opacity: 0; }
  to { transform: scale(1) translateY(0); opacity: 1; }
}

@keyframes containerOut {
  from { transform: scale(1) translateY(0); opacity: 1; }
  to { transform: scale(0.95) translateY(20px); opacity: 0; }
}
</style>
