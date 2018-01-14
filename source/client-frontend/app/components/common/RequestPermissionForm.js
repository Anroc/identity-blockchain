import React, { Component } from 'react';
import ErrorMessage from './ErrorMessage';
import LoadingButton from './LoadingButton';

import { changeRequestForm } from '../../actions';

class Form extends Component {
  constructor(props) {
    super(props);

    this.onSubmit = this.onSubmit.bind(this);

    // new code:
    this.changeGivenName = this.changeGivenName.bind(this);
    this.changeFamilyName = this.changeFamilyName.bind(this);
    this.changeRequest = this.changeRequest.bind(this);
  }

  onSubmit(event) {
    event.preventDefault();
    this.props.onSubmit(this.props.data.givenName, this.props.data.familyName, this.props.data.request);
  }

  changeGivenName(event) {
    this.emitChange({ ...this.props.data, givenName: event.target.value });
  }

  changeFamilyName(event) {
    this.emitChange({ ...this.props.data, familyName: event.target.value });
  }

  changeRequest(event) {
    this.emitChange({ ...this.props.data, request: event.target.value });
  }

  emitChange(newRequestFormState) {
    this.props.dispatch(changeRequestForm(newRequestFormState));
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
