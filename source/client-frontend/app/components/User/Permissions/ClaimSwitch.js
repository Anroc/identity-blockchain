import React from 'react';
import Switch from 'material-ui/Switch';
import { FormControlLabel } from 'material-ui/Form';
import PropTypes from 'prop-types';

class ClaimSwitch extends React.Component {

  constructor() {
    super();
    this.state = {
      showClaim: false,
    };
  }

  componentDidMount() {
    this.isClaimEmpty();
  }

  isClaimEmpty() {
    if (this.props.claims !== {}) {
      this.setState({
        showClaim: true,
      });
    }
  }

  render() {
    return (
      <div key={this.props.claims}>
        { this.state.showClaim ? (
          <div>
            {this.props.claimType}
            <br />
            <FormControlLabel
              control={
                <Switch
                  checked={this.props.claimValue}
                  onChange={() => this.props.changeClaim(this.props.claimType, this.props.claimKey)}
                />
              }
              label={this.props.claimValue ? `Allow sharing information of ${this.props.claimKey}` :
                `Deny sharing information of ${this.props.claimKey}`}
            />
          </div>
      ) : null}
      </div>
    );
  }
}

ClaimSwitch.propTypes = {
  claimType: PropTypes.string,
  claims: PropTypes.object,
  changeClaim: PropTypes.func,
  claimKey: PropTypes.string,
  claimValue: PropTypes.bool,
};

export default ClaimSwitch;
