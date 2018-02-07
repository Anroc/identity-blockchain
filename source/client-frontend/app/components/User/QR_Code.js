import React from 'react';
import PropTypes from 'prop-types';
import Button from 'material-ui/Button';

import LazyImage from '../common/LazyImage';

class QRCode extends React.Component {
  componentDidMount() {
    this.props.showQRCode.bind(this);
  }
  render() {
    return (
      <div>
        <div>
          <p>
            Please verify your account by scanning this QR Code
          </p>
          <Button raised color="primary" onClick={this.props.showQRCode}>
            {this.props.showQR ? 'Hide QR Code' : 'Show QR Code'}
          </Button>
        </div>
        <div>
          {this.props.showQR ?
            <LazyImage
              src="http://srv01.snet.tu-berlin.de:1112/account/qr-code"
              alt="qr-code"
            /> : null}
        </div>
      </div>
    );
  }
}

QRCode.propTypes = {
  showQRCode: PropTypes.func,
  showQR: PropTypes.bool,
};


export default QRCode;
