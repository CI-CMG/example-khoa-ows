import { api } from '@/api';

export default {

  namespaced: true,

  state: {
    loadingFeatures: false,
    features: [],
  },

  mutations: {
    featuresRequest(state) {
      state.loadingFeatures = true;
      state.features = [];
    },
    featuresSuccess(state, { items }) {
      state.loadingFeatures = false;
      state.features = items;
    },
    featuresFailure(state) {
      state.loadingFeatures = false;
    },
  },

  actions: {
    loadFeatures({ commit }) {
      commit('featuresRequest');
      return api.get('/list')
        .then(
          (response) => {
            commit('featuresSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('featuresFailure');
            throw error;
          },
        );
    },
  },

};
