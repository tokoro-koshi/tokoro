const routes = {
  home: '/',
  about: '/about',
  privacyPolicy: '/privacy',
  termsOfService: '/tos',
  contactUs: '/contact',
  blog: '/blog',
  support: '/support',
  place: '/place',
  aiSearch: '/prompt',
  explore: '/explore',
  settings: '/settings',
  admin: '/admin',
  map: '/map',

  auth: {
    login: '/api/auth/login',
    logout: '/api/auth/logout',
    register: '/api/auth/signup',
    me: '/api/auth/me',
  },

  placeholder: '/placeholder.svg',

  api: {},
};

export default routes;
