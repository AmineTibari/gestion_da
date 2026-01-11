<script setup>
import {ref, onMounted, computed} from "vue";
import {useRouter} from "vue-router";

const router = useRouter();

// USER + AUTH
const user = ref(null);

const menu = ref([
  { to: "/admin/demandes", icon: ['far', 'folder-open'], label: "Demandes" },
  { to: "/admin/messagerie", icon: ['fas', 'envelope'], label: "Messagerie" },
  { to: "/admin/statistiques", icon: ['fas', 'chart-line'], label: "Statistiques" },
  { to: "/admin/profil", icon: ['fas', 'user'], label: "Profil" },
]);

onMounted(() => {
  const storedUser = localStorage.getItem("user");
  console.log(storedUser);
  if (!storedUser) router.push("/login");
  const parsed = JSON.parse(storedUser);
  if (!parsed.type || parsed.type !== "ADMINISTRATEUR") {
    router.push("/login");
    return;
  }
  if (parsed.role == "SUPER_ADMIN") {
    menu.value.push({
      to: "/admin/administrateurs",
      icon: ['fas', 'users'],
      label: "Administrateurs"
    });
  }
  user.value = parsed;
});

const logout = () => {
  localStorage.removeItem("user");
  router.push("/login");
};

// SIDEBAR & NOTIFS & TOAST
const sidebarCollapsed = ref(false);


const notifications = ref([
  {id: 1, text: "Nouvelle demande déposée", unread: true, time: "5 min"},
  {id: 2, text: "Demande REQ002 en validation", unread: true, time: "15 min"},
  {id: 3, text: "Message de Sara", unread: true, time: "1h"},
]);

const unreadCount = computed(() =>
    notifications.value.filter(n => n.unread).length
);

const markAsRead = (id = null) => {
  notifications.value.forEach(n => {
    if (!id || n.id === id) n.unread = false;
  });
};






</script>

<template>
  <div class="admin-board">

    <!-- SIDEBAR -->
    <aside :class="['sidebar', { collapsed: sidebarCollapsed }]">
      <div class="sidebar-header">
        <div class="logo-icon">

        </div>
        <transition name="fade">
          <div v-if="!sidebarCollapsed" class="logo-text">
            <h2>AdminPanel</h2>
          </div>
        </transition>
      </div>

      <nav>
        <router-link
            v-for="m in menu"
            :key="m.to"
            :to="m.to"
            class="nav-item"
            active-class="active"
        >
          <FontAwesomeIcon :icon="m.icon" />
          <transition name="fade">
            <span v-if="!sidebarCollapsed">{{ m.label }}</span>
          </transition>
        </router-link>
      </nav>
    </aside>

    <!-- MAIN -->
    <main>
      <!-- ROUTER VIEW -->
      <router-view/>
    </main>
  </div>
</template>

<!-- App.vue -->
<style>
html, body {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
}

* {
  box-sizing: border-box;
}
</style>


<style scoped>

/* Mini style pour la structure (à compléter selon ton design) */
.admin-board {
  display: flex;
  height: 100vh;
  width: 100%;
}


.logo-text {
  font-family: "Poppins", sans-serif;
  font-weight: bold;
}

main {
  flex: 1;
  width: 100%;
  overflow: auto;
}

.sidebar {
  width: 230px;
  background: #1e3a8a;
  color: white;
  padding: 15px;
  background: linear-gradient(180deg, #1e3a8a, #3b82f6);
  box-shadow: 2px 0 8px rgba(0,0,0,0.2);
  transition: width 0.3s, background 0.3s;
}

.sidebar.collapsed {
  width: 70px;
}

.nav-item {
  display: flex;
  gap: 10px;
  padding: 10px;
  color: white;
  text-decoration: none;
  font-family: "Poppins", sans-serif;
}

.nav-item.active {
  background: rgba(255, 255, 255, 0.15);
  border-radius: 6px;
}
.nav-item:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateX(5px);
  color: #7CFC00;
}

.nav-item svg {
  width: 20px;
  height: 20px;
  transition: transform 0.3s, color 0.3s;
}

.nav-item:hover svg {
  transform: scale(1.2);
  color: #7CFC00;
}

.sidebar,
.sidebar * {
  transition: all 0.3s ease-in-out;
}



</style>
