import React, { Component } from 'react';
import ErrorMessage from './ErrorMessage';
import LoadingButton from './LoadingButton';

import { changeForm } from '../../actions';

class Form extends Component {
  constructor(props) {
    super(props);

    this.onSubmit = this.onSubmit.bind(this);
    this.changeUsername = this.changeUsername.bind(this);
    this.changePassword = this.changePassword.bind(this);
    this.changeAccountType = this.changeAccountType.bind(this);

    this.state = {
      accountTypes: ['user', 'provider', '3rd p'],
      selectedAccountType: '',
    };
  }

  onSubmit(event) {
    event.preventDefault();
    this.props.onSubmit(this.props.data.username, this.props.data.password);
  }

  changeUsername(event) {
    this.emitChange({ ...this.props.data, username: event.target.value });
  }

  changePassword(event) {
    this.emitChange({ ...this.props.data, password: event.target.value });
  }

  changeAccountType(event) {
    // this.emitChange({ ...this.props.data, accountType: event.target.value });
    console.log('EVENT: ', event);
    console.log('BEFORE: selectedAccountType:', this.state.selectedAccountType);
    this.setState({
      selectedAccountType: event.target.value,
    });
    console.log('AFTER: selectedAccountType:', this.state.selectedAccountType);
  }

  emitChange(newFormState) {
    this.props.dispatch(changeForm(newFormState));
  }

  render() {
    const { error } = this.props;

    const selectedAccountType = this.state.selectedAccountType;

    const accTypeOptions = (
      <div>
        <div
          key="user"
          className="form__field-radioPad"
        >
          <label
            className={
              this.state.selectedAccountType === 'user' ?
                'form__field-radioPad__wrapper form__field-radioPad__wrapper--selected' :
                'form__field-radioPad__wrapper'
            }
            htmlFor="user"
          >
            <input
              className="form__field-radioPad__radio"
              type="radio"
              name="accountTypes"
              id="user"
              value="user"
              onChange={this.changeAccountType}
            />
            user
          </label>
        </div>
        <div
          key="provider"
          className="form__field-radioPad"
        >
          <label
            className={
              this.state.selectedAccountType === 'provider' ?
                'form__field-radioPad__wrapper form__field-radioPad__wrapper--selected' :
                'form__field-radioPad__wrapper'
            }
            htmlFor="provider"
          >
            <input
              className="form__field-radioPad__radio"
              type="radio"
              name="accountTypes"
              id="provider"
              value="provider"
              onChange={this.changeAccountType}
            />
            provider
          </label>
        </div>
      </div>
    );

    const accountTypeOptions =
      this.state.accountTypes.map((accountType) => {
        const isCurrent = this.state.selectedAccountType === accountType;
        return (
          <div
            key={accountType}
            className="form__field-radioPad"
          >
            <input
              className="form__field-radioPad__radio"
              type="radio"
              name="accountTypes"
              id={accountType}
              value={accountType}
              onChange={this.changeAccountType}
            />
            <label
              className={
                isCurrent ?
                  'form__field-radioPad__wrapper form__field-radioPad__wrapper--selected' :
                  'form__field-radioPad__wrapper'
              }
              htmlFor={accountType}
            >

              {accountType}
            </label>
          </div>
        );
      });

    return (
      <form className="form" onSubmit={this.onSubmit}>
        {error ? <ErrorMessage error={error} /> : null}
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
        <div className="form__submit-btn-wrapper">
          {this.props.currentlySending ? (
            <LoadingButton />
          ) : (
            <button className="form__submit-btn" type="submit">
              {this.props.btnText}
            </button>
             )}
        </div>
      </form>
    );
  }
}

Form.propTypes = {
  dispatch: React.PropTypes.func,
  data: React.PropTypes.object,
  onSubmit: React.PropTypes.func,
  changeForm: React.PropTypes.func,
  btnText: React.PropTypes.string,
  error: React.PropTypes.string,
  currentlySending: React.PropTypes.bool,
};

export default Form;
