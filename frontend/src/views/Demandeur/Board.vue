<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const user = ref(null);

// init user
onMounted(() => {
  const storedUser = localStorage.getItem("user");
  if (!storedUser) {
    router.push("/login");
  } else {
    user.value = JSON.parse(storedUser);
  }
});

// logout function
const logout = () => {
  localStorage.removeItem("user");
  router.push("/login");
};





</script>

<template>

</template>

<style scoped>

</style>