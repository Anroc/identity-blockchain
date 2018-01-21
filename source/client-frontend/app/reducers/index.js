/*
 * The reducer takes care of state changes in our app through actions
 */

import {
  CHANGE_REQUEST_FORM,
  CHANGE_USER_STATE,
  CHANGE_PROVIDER_STATE,
  CHANGE_THIRD_PARTY_STATE,
  CHANGE_FORM,
  SET_AUTH,
  SENDING_REQUEST,
  REQUEST_ERROR,
  CLEAR_ERROR,
} from '../actions/constants';
import auth from '../auth';

// The initial application state
const initialState = {
  userState: {
    password: '',
    ethID: '',
    qrCode: [],
    claims: [],
  },
  providerState: {
    password: '',
    ethID: '',
  },
  thirdPartyState: {
    password: '',
    ethID: '',
  },
  formState: {
    username: '',
    password: '',
    accountType: '',
    // might want to create another form state object
    domainName: '',
  },
  requestFormState: {
    firstName: '',
    givenName: '',
    request: '',
  },
  error: '',
  currentlySending: false,
  loggedIn: auth.loggedIn(),
};

// Takes care of changing the application state
function reducer(state = initialState, action) {
  switch (action.type) {
    case CHANGE_REQUEST_FORM:
      return { ...state, requestFormState: action.newRequestFormState };
    case CHANGE_USER_STATE:
      return { ...state, userState: action.newUserState };
    case CHANGE_PROVIDER_STATE:
      return { ...state, providerState: action.newProviderState };
    case CHANGE_THIRD_PARTY_STATE:
      return { ...state, thirdPartyState: action.newThirdPartyState };

    // stable
    case CHANGE_FORM:
      return { ...state, formState: action.newFormState };
    case SET_AUTH:
      return { ...state, loggedIn: action.newAuthState };
    case SENDING_REQUEST:
      return { ...state, currentlySending: action.sending };
    case REQUEST_ERROR:
      return { ...state, error: action.error };
    case CLEAR_ERROR:
      return { ...state, error: '' };
    default:
      return state;
  }
}

export default reducer;
