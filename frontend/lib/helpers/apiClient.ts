import axios from 'axios';
import { getBearerToken } from '@/lib/helpers/auth0';

const headers = {
  'Content-Type': 'application/json',
};

const apiClient = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL,
  headers: headers,
});

apiClient.interceptors.request.use(async (config) => {
  const token = await getBearerToken();
  if (token) {
    config.headers.Authorization = token;
  }
  return config;
}, Promise.reject);

export default apiClient;
