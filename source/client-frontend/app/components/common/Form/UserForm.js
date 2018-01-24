import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import LoadingButton from '../LoadingButton';

class UserForm extends React.Component {

  constructor() {
    super();
    this.state = {
      a: 0,
    };
  }
  render() {
    return (
      <Fragment>
        <div className="form__field-wrapper">
          <input
            className="form__field-input"
            type="text"
            id="username"
            value={this.props.data.username}
            placeholder="frank.underwood"
            onChange={this.props.changeUsername}
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
            onChange={this.props.changePassword}
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
      </Fragment>
    );
  }
}

UserForm.propTypes = {
  data: PropTypes.object,
  btnText: PropTypes.string,
  currentlySending: PropTypes.bool,
  changeUsername: PropTypes.func,
  changePassword: PropTypes.func,
};

export default UserForm;
