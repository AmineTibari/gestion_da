import { ref } from 'vue'

const toasts = ref([])
let toastId = 0

export const useToast = () => {
    const show = (message, type = 'info', duration = 3000) => {
        const id = ++toastId
        toasts.value.push({ id, message, type, visible: true })

        setTimeout(() => {
            const index = toasts.value.findIndex(t => t.id === id)
            if (index > -1) {
                toasts.value[index].visible = false
                setTimeout(() => {
                    toasts.value = toasts.value.filter(t => t.id !== id)
                }, 300)
            }
        }, duration)
    }

    return {
        success: (msg, duration) => show(msg, 'success', duration),
        error: (msg, duration) => show(msg, 'error', duration),
        warning: (msg, duration) => show(msg, 'warning', duration),
        info: (msg, duration) => show(msg, 'info', duration)
    }
}

export const getToasts = () => toasts
