<script setup>
import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import { useToast } from "@/composables/useToast";

const router = useRouter();
const user = ref(null);
const sidebarCollapsed = ref(false);
const mobileMenuOpen = ref(false);
const toast = useToast();

const menu = ref([
  { to: "/admin/demandes", icon: "folder", label: "Demandes" },
  { to: "/admin/messagerie", icon: "mail", label: "Messagerie" },
  { to: "/admin/statistiques", icon: "chart", label: "Statistiques" },
  { to: "/admin/profil", icon: "user", label: "Profil" },
]);

onMounted(() => {
  const storedUser = localStorage.getItem("user");
  if (!storedUser) {
    router.push("/login");
    return;
  }
  const parsed = JSON.parse(storedUser);
  if (!parsed.type || parsed.type !== "ADMINISTRATEUR") {
    router.push("/login");
    return;
  }
  if (parsed.role === "SUPER_ADMIN") {
    menu.value.push({
      to: "/admin/administrateurs",
      icon: "users",
      label: "Administrateurs"
    });
  }
  user.value = parsed;
});

const logout = () => {
  localStorage.removeItem("user");
  toast.info("Déconnexion réussie");
  router.push("/login");
};

const userInitials = computed(() => {
  if (!user.value) return "";
  return (user.value.nom?.[0] || "") + (user.value.prenom?.[0] || "");
});

const closeMobileMenu = () => {
  mobileMenuOpen.value = false;
};
</script>

<template>
  <div class="admin-layout">
    <!-- Mobile Header -->
    <header class="mobile-header">
      <button class="hamburger" @click="mobileMenuOpen = !mobileMenuOpen">
        <span></span>
        <span></span>
        <span></span>
      </button>
      <span class="mobile-title">AdminPanel</span>
      <div class="mobile-avatar" v-if="user">{{ userInitials }}</div>
    </header>

    <!-- Mobile Overlay -->
    <div class="mobile-overlay" :class="{ active: mobileMenuOpen }" @click="closeMobileMenu"></div>

    <!-- Sidebar -->
    <aside :class="['sidebar', { collapsed: sidebarCollapsed, 'mobile-open': mobileMenuOpen }]">
      <!-- Logo -->
      <div class="sidebar-header">
        <div class="logo">
          <div class="logo-icon">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none">
              <path d="M12 2L2 7l10 5 10-5-10-5z" stroke="url(#logoGrad)" stroke-width="2" stroke-linecap="round"/>
              <path d="M2 17l10 5 10-5" stroke="url(#logoGrad)" stroke-width="2" stroke-linecap="round"/>
              <path d="M2 12l10 5 10-5" stroke="url(#logoGrad)" stroke-width="2" stroke-linecap="round"/>
              <defs>
                <linearGradient id="logoGrad" x1="0%" y1="0%" x2="100%" y2="100%">
                  <stop offset="0%" style="stop-color:#06b6d4"/>
                  <stop offset="100%" style="stop-color:#8b5cf6"/>
                </linearGradient>
              </defs>
            </svg>
          </div>
          <transition name="fade">
            <span v-if="!sidebarCollapsed" class="logo-text">AdminPanel</span>
          </transition>
        </div>
        <button class="collapse-btn desktop-only" @click="sidebarCollapsed = !sidebarCollapsed">
          <svg :class="{ rotated: sidebarCollapsed }" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="15 18 9 12 15 6"/>
          </svg>
        </button>
        <button class="close-mobile" @click="closeMobileMenu">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/>
          </svg>
        </button>
      </div>

      <!-- Navigation -->
      <nav class="sidebar-nav">
        <router-link
          v-for="item in menu"
          :key="item.to"
          :to="item.to"
          class="nav-item"
          active-class="active"
          @click="closeMobileMenu"
        >
          <svg v-if="item.icon === 'folder'" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M22 19a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h5l2 3h9a2 2 0 0 1 2 2z"/>
          </svg>
          <svg v-else-if="item.icon === 'mail'" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
            <polyline points="22,6 12,13 2,6"/>
          </svg>
          <svg v-else-if="item.icon === 'chart'" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="18" y1="20" x2="18" y2="10"/>
            <line x1="12" y1="20" x2="12" y2="4"/>
            <line x1="6" y1="20" x2="6" y2="14"/>
          </svg>
          <svg v-else-if="item.icon === 'user'" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
            <circle cx="12" cy="7" r="4"/>
          </svg>
          <svg v-else-if="item.icon === 'users'" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
            <circle cx="9" cy="7" r="4"/>
            <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
            <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
          </svg>
          
          <transition name="fade">
            <span v-if="!sidebarCollapsed">{{ item.label }}</span>
          </transition>
        </router-link>
      </nav>

      <!-- User Profile -->
      <div class="sidebar-footer" v-if="user">
        <div class="user-info">
          <div class="user-avatar">{{ userInitials }}</div>
          <transition name="fade">
            <div v-if="!sidebarCollapsed" class="user-details">
              <span class="user-name">{{ user.nom }}</span>
              <span class="user-role">{{ user.role || 'Admin' }}</span>
            </div>
          </transition>
        </div>
        <button class="logout-btn" @click="logout" :title="sidebarCollapsed ? 'Déconnexion' : ''">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
            <polyline points="16 17 21 12 16 7"/>
            <line x1="21" y1="12" x2="9" y2="12"/>
          </svg>
        </button>
      </div>
    </aside>

    <!-- Main Content -->
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<style scoped>
.admin-layout {
  display: flex;
  min-height: 100vh;
  background: linear-gradient(135deg, #0f172a 0%, #1e1b4b 100%);
}

/* Mobile Header */
.mobile-header {
  display: none;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: rgba(15, 23, 42, 0.95);
  backdrop-filter: blur(16px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  padding: 0 16px;
  align-items: center;
  justify-content: space-between;
  z-index: 90;
}

.hamburger {
  width: 40px;
  height: 40px;
  background: rgba(255, 255, 255, 0.05);
  border: none;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 5px;
  cursor: pointer;
}

.hamburger span {
  width: 20px;
  height: 2px;
  background: white;
  border-radius: 2px;
  transition: all 0.3s;
}

.mobile-title {
  font-size: 18px;
  font-weight: 700;
  background: linear-gradient(135deg, #06b6d4, #8b5cf6);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.mobile-avatar {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, #8b5cf6, #06b6d4);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 700;
  font-size: 14px;
}

.mobile-overlay {
  display: none;
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.6);
  z-index: 95;
  opacity: 0;
  visibility: hidden;
  transition: all 0.3s;
}

.mobile-overlay.active {
  opacity: 1;
  visibility: visible;
}

.close-mobile {
  display: none;
  background: none;
  border: none;
  color: rgba(255, 255, 255, 0.5);
  cursor: pointer;
}

/* Sidebar */
.sidebar {
  width: 260px;
  background: rgba(15, 23, 42, 0.95);
  backdrop-filter: blur(20px);
  border-right: 1px solid rgba(255, 255, 255, 0.08);
  display: flex;
  flex-direction: column;
  transition: all 0.3s ease;
  position: relative;
  z-index: 100;
}

.sidebar.collapsed {
  width: 80px;
}

/* Header */
.sidebar-header {
  padding: 24px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  width: 44px;
  height: 44px;
  background: linear-gradient(135deg, rgba(6, 182, 212, 0.15), rgba(139, 92, 246, 0.15));
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.logo-text {
  font-size: 18px;
  font-weight: 700;
  background: linear-gradient(135deg, #06b6d4, #8b5cf6);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  white-space: nowrap;
}

.collapse-btn {
  width: 32px;
  height: 32px;
  background: rgba(255, 255, 255, 0.05);
  border: none;
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.5);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.collapse-btn:hover {
  background: rgba(255, 255, 255, 0.1);
  color: white;
}

.collapse-btn svg {
  transition: transform 0.3s ease;
}

.collapse-btn svg.rotated {
  transform: rotate(180deg);
}

/* Navigation */
.sidebar-nav {
  flex: 1;
  padding: 20px 12px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 14px 16px;
  color: rgba(255, 255, 255, 0.6);
  text-decoration: none;
  border-radius: 12px;
  transition: all 0.2s;
  position: relative;
  overflow: hidden;
}

.nav-item::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background: linear-gradient(180deg, #06b6d4, #8b5cf6);
  opacity: 0;
  transition: opacity 0.2s;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.05);
  color: white;
}

.nav-item.active {
  background: linear-gradient(135deg, rgba(6, 182, 212, 0.15), rgba(139, 92, 246, 0.15));
  color: white;
}

.nav-item.active::before {
  opacity: 1;
}

.nav-item svg {
  flex-shrink: 0;
}

.nav-item span {
  font-size: 15px;
  font-weight: 500;
  white-space: nowrap;
}

/* Footer */
.sidebar-footer {
  padding: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 0;
}

.user-avatar {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #8b5cf6, #06b6d4);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 700;
  font-size: 14px;
  flex-shrink: 0;
}

.user-details {
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.user-name {
  color: white;
  font-size: 14px;
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-role {
  color: rgba(255, 255, 255, 0.5);
  font-size: 12px;
}

.logout-btn {
  width: 36px;
  height: 36px;
  background: rgba(239, 68, 68, 0.1);
  border: none;
  border-radius: 10px;
  color: #f87171;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
  flex-shrink: 0;
}

.logout-btn:hover {
  background: rgba(239, 68, 68, 0.2);
  color: #ef4444;
}

/* Main Content */
.main-content {
  flex: 1;
  overflow-y: auto;
  background: linear-gradient(180deg, rgba(15, 23, 42, 0.5) 0%, rgba(30, 27, 75, 0.5) 100%);
}

/* Transitions */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* Responsive */
@media (max-width: 768px) {
  .mobile-header {
    display: flex;
  }
  
  .mobile-overlay {
    display: block;
  }
  
  .close-mobile {
    display: block;
  }
  
  .desktop-only {
    display: none;
  }
  
  .sidebar {
    position: fixed;
    left: -280px;
    top: 0;
    height: 100vh;
    z-index: 100;
    width: 280px;
  }
  
  .sidebar.mobile-open {
    left: 0;
  }
  
  .sidebar.collapsed {
    width: 280px;
  }
  
  .main-content {
    width: 100%;
    padding-top: 60px;
  }
}
</style>
