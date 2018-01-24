import actualRequest from './actualRequest';
import request from './request';

const auth = {
  /**
   * Logs a user in, returning a promise with `true` when done
   * @param  {string} password The password of the user
   */
  login(password) {
    if (auth.loggedIn()) {
      return Promise.resolve(true);
    }
    // post a login request
    return actualRequest.post('login', { password })
      .then((response) => {
        if (response.status >= 200 && response.status < 300) {
          return response;
        }
        const error = Error(response.statusText);
        error.response = response;
        throw error;
      }).then((response) => {
        if (response.status === 204 || response.status === 205) {
          return null;
        }
        return response.json();
      });
    /*
    // Post a fake request
    return request.post('/login', { username, password })
      .then((response) => {
        // Save token to local storage
        localStorage.token = response.token;
        return Promise.resolve(true);
      });
    */
  },
  /**
   * Logs the current user out
   */
  logout() {
    return request.post('/logout');
  },
  /**
   * Checks if a user is logged in
   */
  loggedIn() {
    return !!localStorage.token;
  },
  /**
   * Registers a user and then logs them in
   * @param  {string} username The username of the user
   * @param  {string} password The password of the user
   */
  register(username, password) {
    // post an actual request
    // post a login request
    return actualRequest.post('register', { password })
      .then((response) => {
        if (response.status >= 200 && response.status < 300) {
          return response;
        }
        const error = Error(response.statusText);
        error.response = response;
        throw error;
      }).then((response) => {
        if (response.status === 204 || response.status === 205) {
          return null;
        }
        return response.json();
      }).then(() => auth.login(password));
    /*
    // Post a fake request
    return request.post('/login', { username, password })
      .then((response) => {
        // Save token to local storage
        localStorage.token = response.token;
        return Promise.resolve(true);
      });
    */
    /*
    // Post a fake request
    return request.post('/register', { username, password })
    // Log user in after registering
      .then(() => auth.login(username, password));
    */
  },
  onChange() {},
};

export default auth;
