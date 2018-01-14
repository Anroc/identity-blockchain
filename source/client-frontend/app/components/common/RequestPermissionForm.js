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

    // new code:
    this.changeGivenName = this.changeGivenName.bind(this);
    this.changeFamilyName = this.changeFamilyName.bind(this);
  }

  onSubmit(event) {
    event.preventDefault();
    this.props.onSubmit(this.props.data.username, this.props.data.password);
  }

  changeGivenName(event) {
    this.emitChange({ ...this.props.data, username: event.target.value });
  }

  changeFamilyName(event) {
    this.emitChange({ ...this.props.data, username: event.target.value });
  }

  changePassword(event) {
    this.emitChange({ ...this.props.data, password: event.target.value });
  }

  changeRequest(event) {
    this.emitChange({ ...this.props.data, request: event.target.value });
  }

  emitChange(newFormState) {
    this.props.dispatch(changeForm(newFormState));
  }

  render() {
    const { error } = this.props;

    return (
      <form className="form" onSubmit={this.onSubmit}>
        {error ? <ErrorMessage error={error} /> : null}
        <div className="form__field-wrapper">
          <input
            className="form__field-input"
            type="text"
            id="username"
            value={this.props.data.givenName}
            placeholder="frank"
            onChange={this.changeGivenName}
            autoCorrect="off"
            autoCapitalize="off"
            spellCheck="false"
          />
          <label className="form__field-label" htmlFor="username">
            Given Name
          </label>
        </div>

        <div className="form__field-wrapper">
          <input
            className="form__field-input"
            type="text"
            id="username"
            value={this.props.data.familyName}
            placeholder="underwood"
            onChange={this.changeFamilyName}
            autoCorrect="off"
            autoCapitalize="off"
            spellCheck="false"
          />
          <label className="form__field-label" htmlFor="username">
            Family Name
          </label>
        </div>

        <div className="form__field-wrapper">
          <input
            className="form__field-input"
            id="request"
            type="checkbox"
            value={this.props.data.requests}
            placeholder="isOver18"
            onChange={this.changeRequest}
          />
          <label className="form__field-label" htmlFor="requests">
            Request
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
