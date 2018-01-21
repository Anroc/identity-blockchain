const getUserOptions = {
  method: 'GET',
  headers: {
    Authorization: 'Basic YWRtaW46cGVuaXNwdW1wZQ==',
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
  mode: 'cors',
  credentials: 'include',
};

const postUserOptions = {
  method: 'POST',
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    password: 'timsDickerDick',
  }),
  mode: 'cors',
  credentials: 'include',
};

const getUserClaimOptions = {
  method: 'POST',
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({}),
  mode: 'cors',
  credentials: 'include',
};

export {
  getUserOptions,
  postUserOptions,
  getUserClaimOptions,
};
