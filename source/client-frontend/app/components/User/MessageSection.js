import React from 'react';
import PropTypes from 'prop-types';
import Button from 'material-ui/Button';
import MessagesTable from './MessagesTable';

class MessageSection extends React.Component {
  componentDidMount() {
  }

  render() {
    return (
      <section>
        <Button
          onClick={this.props.getMessages}
        >
          Get Messages
        </Button>
        <div>
          {this.props.messages && JSON.stringify(this.props.messages)}
        </div>
        { this.props.messages && (
          <MessagesTable messages={this.props.messages} />
        )}
      </section>
    );
  }
}

MessageSection.propTypes = {
  getMessages: PropTypes.func,
  messages: PropTypes.array,
};

export default MessageSection;
