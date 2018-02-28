const getUserOptions = {
  method: 'GET',
  headers: {
    Authorization: 'Basic YWRtaW46cGFzc3dvcmQ=',
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
  mode: 'cors',
  credentials: 'include',
};

const postUserOptions = (password) => ({
  method: 'POST',
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    password,
  }),
  mode: 'cors',
  credentials: 'include',
});

export {
  getUserOptions,
  postUserOptions,
};
