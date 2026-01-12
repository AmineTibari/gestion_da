<script setup>
const props = defineProps({
  show: Boolean,
  title: { type: String, default: 'Confirmation' },
  message: String,
  confirmText: { type: String, default: 'Confirmer' },
  cancelText: { type: String, default: 'Annuler' },
  type: { type: String, default: 'warning' } // warning, danger, info
})

const emit = defineEmits(['confirm', 'cancel'])

const confirm = () => emit('confirm')
const cancel = () => emit('cancel')
</script>

<template>
  <Teleport to="body">
    <Transition name="confirm">
      <div v-if="show" class="confirm-overlay" @click="cancel">
        <div class="confirm-container" @click.stop>
          <div :class="['confirm-icon', type]">
            <svg v-if="type === 'warning'" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
            </svg>
            <svg v-else-if="type === 'danger'" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <path d="M15 9l-6 6M9 9l6 6"/>
            </svg>
            <svg v-else width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <path d="M12 16v-4M12 8h.01"/>
            </svg>
          </div>
          
          <h3 class="confirm-title">{{ title }}</h3>
          <p class="confirm-message">{{ message }}</p>
          
          <div class="confirm-actions">
            <button class="btn-cancel" @click="cancel">{{ cancelText }}</button>
            <button :class="['btn-confirm', type]" @click="confirm">{{ confirmText }}</button>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<style scoped>
.confirm-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.7);
  backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
  padding: 24px;
}

.confirm-container {
  background: linear-gradient(145deg, rgba(30, 41, 59, 0.98), rgba(15, 23, 42, 0.98));
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 20px;
  padding: 32px;
  text-align: center;
  max-width: 400px;
  width: 100%;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.5);
}

.confirm-icon {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
}

.confirm-icon.warning {
  background: rgba(245, 158, 11, 0.15);
  color: #fbbf24;
}

.confirm-icon.danger {
  background: rgba(239, 68, 68, 0.15);
  color: #f87171;
}

.confirm-icon.info {
  background: rgba(6, 182, 212, 0.15);
  color: #22d3ee;
}

.confirm-title {
  color: white;
  font-family: 'Inter', sans-serif;
  font-size: 20px;
  font-weight: 600;
  margin: 0 0 12px;
}

.confirm-message {
  color: rgba(255, 255, 255, 0.6);
  font-family: 'Inter', sans-serif;
  font-size: 15px;
  line-height: 1.6;
  margin: 0 0 28px;
}

.confirm-actions {
  display: flex;
  gap: 12px;
}

.btn-cancel,
.btn-confirm {
  flex: 1;
  padding: 12px 24px;
  border-radius: 12px;
  font-family: 'Inter', sans-serif;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-cancel {
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: rgba(255, 255, 255, 0.7);
}

.btn-cancel:hover {
  background: rgba(255, 255, 255, 0.12);
  color: white;
}

.btn-confirm {
  border: none;
  color: white;
}

.btn-confirm.warning {
  background: linear-gradient(135deg, #f59e0b, #d97706);
}

.btn-confirm.danger {
  background: linear-gradient(135deg, #ef4444, #dc2626);
}

.btn-confirm.info {
  background: linear-gradient(135deg, #06b6d4, #0891b2);
}

.btn-confirm:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.3);
}

/* Animations */
.confirm-enter-active { animation: confirmIn 0.3s ease; }
.confirm-leave-active { animation: confirmOut 0.2s ease; }

.confirm-enter-active .confirm-container { animation: containerIn 0.3s ease; }
.confirm-leave-active .confirm-container { animation: containerOut 0.2s ease; }

@keyframes confirmIn { from { opacity: 0; } to { opacity: 1; } }
@keyframes confirmOut { from { opacity: 1; } to { opacity: 0; } }

@keyframes containerIn {
  from { transform: scale(0.9); opacity: 0; }
  to { transform: scale(1); opacity: 1; }
}

@keyframes containerOut {
  from { transform: scale(1); opacity: 1; }
  to { transform: scale(0.9); opacity: 0; }
}
</style>
