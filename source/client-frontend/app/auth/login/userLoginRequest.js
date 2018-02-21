import request from '../request';

function userLoginRequest(password) {
  console.log('sending register request');
  const options = {
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
  };

  const actualRequest = request('http://localhost:8080/account/login', options)
    .then((json) => {
      console.log(`content' + ${JSON.stringify(json)}`);
      this.setState({
        ethId: json,
      });
    });

  console.log(`actual ${actualRequest}`);
}

export default userLoginRequest;
