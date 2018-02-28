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
            {Object.entries(this.props.claims).map(([key, value]) => (
              <FormControlLabel
                control={
                  <Switch
                    checked={this.props.claims[key]}
                    onChange={() => this.props.changeClaim(this.props.claimType, key)}
                  />
                }
                label={this.props.claims[key] ? `Allow sharing information of ${key}` :
                  `Allow sharing information of ${key}`}
              />
          ))}
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
};

export default ClaimSwitch;
