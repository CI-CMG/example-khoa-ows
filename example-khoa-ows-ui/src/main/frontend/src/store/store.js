import Vue from 'vue';
import Vuex from 'vuex';
import app from './modules/appModule';
import ows from './modules/owsModule';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    app,
    ows,
  },
});
