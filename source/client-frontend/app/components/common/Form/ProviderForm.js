import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import LoadingButton from '../LoadingButton';

class ProviderForm extends React.Component {

  render() {
    return (
      <Fragment>
        <div className="form__field-wrapper">
          <input
            className="form__field-input"
            type="text"
            id="providerName"
            value={this.props.data.username}
            placeholder="telekom"
            onChange={this.props.changeUsername}
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
            onChange={this.props.changeDomainName}
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

ProviderForm.propTypes = {
  data: PropTypes.object,
  btnText: PropTypes.string,
  currentlySending: PropTypes.bool,
  changeUsername: PropTypes.func,
  changePassword: PropTypes.func,
  changeDomainName: PropTypes.func,
};

export default ProviderForm;
