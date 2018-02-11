import React from 'react';
import Switch from 'material-ui/Switch';
import { FormControlLabel } from 'material-ui/Form';
import PropTypes from 'prop-types';

class ClaimSwitch extends React.Component {

  constructor() {
    super();
    this.state = {};
  }

  componentDidMount() {
    Object.entries(this.props.claims).forEach(([key, value]) => {
      this.setState({
        [key]: value,
      });
      console.log(`setting state for: ${key} to ${value}`);
    });
  }

  render() {
    return (
      <div key={this.props.claims}>
        {this.props.claimType}
        <br />
        {Object.entries(this.props.claims).map(([key, value]) => (
          <FormControlLabel
            control={
              <Switch
                checked={this.state[key]}
                onChange={() => this.setState({ [key]: !this.state[key] })}
              />
            }
            label={key}
          />
        ))}
      </div>
    );
  }
}

ClaimSwitch.propTypes = {
  claimType: PropTypes.string,
  claims: PropTypes.object,
};

export default ClaimSwitch;
