<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const user = ref(null);

onMounted(() => {
  const storedUser = localStorage.getItem("user");
  console.log(storedUser);

  if (!storedUser) {
    router.push("/login");
  } else {
    user.value = JSON.parse(storedUser);
  }
});

const logout = () => {
  localStorage.removeItem("user");
  router.push("/login");
};
</script>

<template>
  <div class="dashboard">
    <h1>Dashboard</h1>

    <div v-if="user">
      <p>Bienvenue, {{ user.name }} !</p>
      <p>Email: {{ user.email }}</p>
      <p>Role: {{ user.role ? user.role : "Utilisateur" }}</p>
    </div>

    <button @click="logout">Déconnexion</button>
  </div>
</template>

<style scoped>
.dashboard {
  max-width: 600px;
  margin: 40px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 10px;
}
button {
  margin-top: 20px;
  padding: 8px 16px;
  cursor: pointer;
}
</style>
