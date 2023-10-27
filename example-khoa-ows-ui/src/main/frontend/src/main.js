import './resourceBasePath';
import Vue from 'vue';
import router from '@/router/router';
import '@/assets/css/main.scss';
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue';
import store from '@/store/store';
import App from './App.vue';

Vue.config.productionTip = false;

Vue.use(BootstrapVue);
Vue.use(IconsPlugin);

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount('#app');
