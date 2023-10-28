import { api } from '@/api';

export default {

  namespaced: true,

  state: {
    loadingFeatures: false,
    features: [],
    savingFeature: false,
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
    saveRequest(state) {
      state.savingFeature = true;
    },
    saveSuccess(state) {
      state.savingFeature = false;
    },
    saveFailure(state) {
      state.savingFeature = false;
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
    save({ commit }, { feature, endpoint }) {
      commit('saveRequest');
      api.post(`/${endpoint}`, feature)
        .then(
          (response) => {
            commit('saveSuccess', response.data);
            return response.data;
          },
          (error) => {
            commit('saveFailure');
            const { response } = error;
            if (response) {
              const { data } = response;
              if (data) {
                const { formErrors } = data;
                if (formErrors) {
                  const paths = Object.keys(formErrors);
                  paths.forEach((path) => {
                    const message = formErrors[path].join(', ');
                    commit('featureForm/setTouched', { path, touched: false }, { root: true });
                    commit('featureForm/setError', { path, error: message }, { root: true });
                  });
                }
              }
            }
            throw error;
          },
        );
    },
  },

};
