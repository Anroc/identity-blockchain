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

    /*
    Object.entries(this.props.claims).forEach(([key, value]) => {
      this.setState({
        [key]: value,
      });
      console.log(`setting state for: ${key} to ${value}`);
    });
    */
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
                label={key}
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
