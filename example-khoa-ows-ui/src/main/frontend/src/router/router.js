import Vue from 'vue';
import VueRouter from 'vue-router';
import { register } from '@/apiMiddleware';
import View from '@/views/view/View.vue';
import Main from '@/views/view/main/Main.vue';
import Ows from '@/views/view/main/ows/Ows.vue';
import { RAW_BASE_PATH } from '@/resourceBasePath';
import Swagger from '@/views/view/main/swagger/Swagger.vue';

Vue.use(VueRouter);

const routes = [
  {
    path: '/view',
    component: View,
    name: 'View',
    redirect: { name: 'Ows' },
    children: [
      {
        path: 'main',
        name: 'Main',
        component: Main,
        redirect: { name: 'Ows' },
        children: [
          {
            path: 'ows',
            name: 'Ows',
            component: Ows,
          },
          {
            path: 'api-docs',
            name: 'Swagger',
            component: Swagger,
          },
          {
            path: '*',
            redirect: { name: 'Main' },
          },
        ],
      },
      {
        path: '*',
        redirect: { name: 'View' },
      },
    ],
  },
  {
    path: '*',
    redirect: { name: 'View' },
  },
];

const router = new VueRouter({
  mode: 'history',
  base: `${RAW_BASE_PATH}`,
  routes,
  linkActiveClass: 'active',
});

register(router);

export default router;
