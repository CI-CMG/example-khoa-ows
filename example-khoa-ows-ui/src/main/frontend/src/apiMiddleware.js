import store from '@/store/store';
import {
  api,
} from './api';

const onSuccess = (response) => response;
const onError = (error) => {
  let msg = ['An Unknown Error Occurred'];
  if (error.response && error.response.data && error.response.data.formErrors && error.response.data.formErrors.length) {
    msg = error.response.data.formErrors;
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
