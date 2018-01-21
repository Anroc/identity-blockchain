import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import ErrorMessage from '../ErrorMessage';
import LoadingButton from '../LoadingButton';

import { changeForm } from '../../../actions/index';

class Form extends Component {
  constructor(props) {
    super(props);
    this.state = {
      accountTypes: ['user', 'provider', 'thirdParty'],
    };

    this.onSubmit = this.onSubmit.bind(this);
    this.changeUsername = this.changeUsername.bind(this);
    this.changePassword = this.changePassword.bind(this);
    this.changeAccountType = this.changeAccountType.bind(this);
    // provider
    this.changeDomainName = this.changeDomainName.bind(this);
  }

  componentDidMount() {
    // this.props.data.accountType = 'user';
  }

  onSubmit(event) {
    event.preventDefault();
    this.props.onSubmit(this.props.data.username, this.props.data.password, this.props.data.accountType);
  }

  changeDomainName(event) {
    this.emitChange({ ...this.props.data, domainName: event.target.value });
  }

  changeUsername(event) {
    this.emitChange({ ...this.props.data, username: event.target.value });
  }

  changePassword(event) {
    this.emitChange({ ...this.props.data, password: event.target.value });
  }

  changeAccountType(event) {
    this.emitChange({ ...this.props.data, accountType: event.target.value });
  }

  emitChange(newFormState) {
    this.props.dispatch(changeForm(newFormState));
  }

  render() {
    const { error } = this.props;

    const accountTypeOptions =
      this.state.accountTypes.map((accountType) => {
        const isCurrentlySelectedAccountType = this.props.data.accountType === accountType;
        return (
          <div
            key={accountType}
            className="form__field-radioPad"
          >
            <label
              className={
                isCurrentlySelectedAccountType ?
                  'form__field-radioPad__wrapper form__field-radioPad__wrapper--selected' :
                  'form__field-radioPad__wrapper'
              }
              htmlFor={accountType}
            >
              <input
                className="form__field-radioPad__radio"
                type="radio"
                name="accountTypes"
                id={accountType}
                value={accountType}
                onChange={this.changeAccountType}
              />

              {accountType}
            </label>
          </div>
        );
      });

    return (
      <form className="form" onSubmit={this.onSubmit}>
        <div>
          account type {this.props.data.accountType}
        </div>
        {error ? <ErrorMessage error={error} /> : null}
        <div className="form__field-wrapper">
          <div className="container text-center">
            <div className="row">
              {accountTypeOptions}
              <label className="form__field-label" htmlFor="userAccountType">
                Account Type
              </label>
            </div>
          </div>
        </div>
        {this.props.data.accountType === 'user' ?
          <Fragment>
            <div className="form__field-wrapper">
              <input
                className="form__field-input"
                type="text"
                id="username"
                value={this.props.data.username}
                placeholder="frank.underwood"
                onChange={this.changeUsername}
                autoCorrect="off"
                autoCapitalize="off"
                spellCheck="false"
              />
              <label className="form__field-label" htmlFor="username">
                Username
              </label>
            </div>
            <div className="form__field-wrapper">
              <input
                className="form__field-input"
                id="password"
                type="password"
                value={this.props.data.password}
                placeholder="••••••••••"
                onChange={this.changePassword}
              />
              <label className="form__field-label" htmlFor="password">
                Password
              </label>
            </div>
            <div className="form__submit-btn-wrapper">
              {this.props.currentlySending ? (
                <LoadingButton />
              ) : (
                <button className="form__submit-btn" type="submit">
                  {this.props.btnText}
                </button>
              )}
            </div>
          </Fragment> :
          <Fragment>
            <div className="form__field-wrapper">
              <input
                className="form__field-input"
                type="text"
                id="providerName"
                value={this.props.data.username}
                placeholder="telekom"
                onChange={this.changeUsername}
                autoCorrect="off"
                autoCapitalize="off"
                spellCheck="false"
              />
              <label className="form__field-label" htmlFor="providerName">
                Provider name
              </label>
            </div>
            <div className="form__field-wrapper">
              <input
                className="form__field-input"
                type="text"
                id="domainName"
                value={this.props.data.domainName || ''}
                placeholder="www.snet.tu-berlin.de"
                onChange={this.changeDomainName}
                autoCorrect="off"
                autoCapitalize="off"
                spellCheck="false"
              />
              <label className="form__field-label" htmlFor="domainName">
                Domain name
              </label>
            </div>
            <div className="form__field-wrapper">
              <input
                className="form__field-input"
                id="password"
                type="password"
                value={this.props.data.password}
                placeholder="••••••••••"
                onChange={this.changePassword}
              />
              <label className="form__field-label" htmlFor="password">
                Password
              </label>
            </div>
            <div className="form__submit-btn-wrapper">
              {this.props.currentlySending ? (
                <LoadingButton />
              ) : (
                <button className="form__submit-btn" type="submit">
                  {this.props.btnText}
                </button>
                 )}
            </div>
          </Fragment>}
      </form>
    );
  }
}

Form.propTypes = {
  dispatch: PropTypes.func,
  data: PropTypes.object,
  onSubmit: PropTypes.func,
  changeForm: PropTypes.func,
  btnText: PropTypes.string,
  error: PropTypes.string,
  currentlySending: PropTypes.bool,
};

export default Form;
