import request from '../request';

function* userRegistrationRequest(password) {
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
  // TODO currently setting it in wrong state
  yield request('http://localhost:8080/account/register', options)
    .then((json) => {
      const ethId = json.ethereumID;
      console.log('ETH ID', ethId);
      console.log(`content' + ${JSON.stringify(json)}`);
      return ethId;
    });
}

export default userRegistrationRequest;
