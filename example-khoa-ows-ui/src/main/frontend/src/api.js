import axios from 'axios';
import {
  BASE_PATH,
  API,
} from '@/basePath';

const defaultTimeout = 60000;

const api = axios.create();

const initialize = () => {
  api.defaults.baseURL = `${BASE_PATH}${API}`;
  api.defaults.timeout = defaultTimeout;
};

initialize();

export {
  api,
};
