import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";

// FontAwesome
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faFolderOpen } from '@fortawesome/free-regular-svg-icons'
import { faEnvelope, faChartLine, faUsers, faUser, faEye } from '@fortawesome/free-solid-svg-icons'


library.add(faFolderOpen, faEnvelope, faChartLine, faUsers, faUser, faEye)



createApp(App)
    .use(router)
    .component('FontAwesomeIcon', FontAwesomeIcon)
    .mount("#app");

