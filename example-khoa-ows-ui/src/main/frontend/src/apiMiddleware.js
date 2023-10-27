import store from '@/store/store';
import {
  api,
} from './api';

const onSuccess = (response) => response;
const onError = (error, router) => {
  let msg = ['An Unknown Error Occurred'];
  if (error.response && error.response.data && error.response.data.formErrors && error.response.data.formErrors.length) {
      msg = error.response.data.formErrors;
  }
  if (error.response && (error.response.status === 401 || error.response.status === 403)) {
    logout(router, store);
    router.push({ name: 'Home' });
  }
  store.commit('app/addErrors', msg);
  throw error;
};

const intercept = (service, router) => {
  service.interceptors.response.use(onSuccess, (error) => onError(error, router));
};

export const register = (router) => {
  intercept(api, router);
};
