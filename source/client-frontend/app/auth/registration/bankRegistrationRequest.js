import request from '../request';

function bankRegistrationRequest(password) {
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

  const actualRequest = request('http://srv01.snet.tu-berlin.de:8102/account/register', options)
    .then((json) => {
      console.log(`content bank registration ' + ${JSON.stringify(json)}`);
      this.setState({
        ethId: json,
      });
    });
  console.log(`actual ${actualRequest}`);
}

export default bankRegistrationRequest;
