import React from 'react';
import ReactDOM from 'react-dom';
import {EntityView, NewEntityView} from './entity.js'
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'
import Popup from 'react-popup';
import Prompt from 'common-components/lib/prompt.js'
import {PromptTemplate} from 'common-components/lib/prompt-template.js'
import {Menu} from 'common-components/lib/menu.js'
import $ from 'jquery'

import './index.css'
import 'common-components/css/popup.css'

class App extends React.Component {
  render() {
    return (
      <div>
        <Router>
          <Switch>
            <Route path="/entityeditor/:entitykey/edit" component={EditEntity}/>
            <Route path="/entityeditor/:entitykey/view" component={ViewEntity}/>
            <Route path="/entityeditor/:templateKey/new" component={NewEntity}/>
            <Route component={Default}/>
          </Switch>
        </Router>
      </div>
    )
  }
};

const Default = ({match}) => {
  var params={};window.location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi,function(s,k,v){params[k]=v});

  if (params['entitykey'] != null) {
    return React.createElement(EntityView, {entitykey: params['entitykey']});
  } else if (params['templatekey'] != null) {
    return React.createElement(NewEntityView, {templatekey: params['templatekey']});
  } else {
    return (<div/>);
  }
}

const EditEntity = ({match}) => {
  return React.createElement(EntityView, {entitykey: match.params.entitykey});
}

const ViewEntity = ({match}) => {
  return React.createElement(EntityView, {entitykey: match.params.entitykey});
}

const NewEntity = ({match}) => {
  return React.createElement(NewEntityView, {templatekey: match.params.templateKey});
}

// Default Component
ReactDOM.render(
  React.createElement(App, {}),
  document.getElementById('entityview')
);

ReactDOM.render(
    <Popup />,
    document.getElementById('popupContainer')
);

ReactDOM.render(
    <Menu items={[
      {
        id: 'new',
        name: 'New',
        onClick: () => {

          Popup.plugins().promptTemplate('create', 'Type your name', function (value) {

            ReactDOM.unmountComponentAtNode(document.getElementById('entityview'));

            ReactDOM.render(
              React.createElement(NewEntityView, {templatekey: value}),
              document.getElementById('entityview')
            );
          });
        }
      }, {
        id: 'load',
        name: 'Load',
        onClick: () => {
          Popup.plugins().prompt('Load', 'Entity Selection', '', 'Enter Entity Key', function (value) {

            ReactDOM.unmountComponentAtNode(document.getElementById('entityview'));

            ReactDOM.render(
              React.createElement(EntityView, {entitykey: value}),
              document.getElementById('entityview')
            );
          });
        }
      }, {
        id: 'save',
        name: 'Save',
        onClick: () => {
          alert("TODO: Not Implemented");
        }
      }, {
        id: 'reset',
        name: 'reset',
        onClick: () => {
          alert("TODO: Not Implemented");
        }
      }
    ]}/>,
    document.getElementById('myNavbar')
);

function saveChanges(data) {

  $.ajax({
    type : "POST",
    data : JSON.stringify(data),
    contentType : "application/json; charset=utf-8",
    url : 'http://localhost:8181/entity',
    success : function (data) {
      alert('success');
    },
    error : function (data) {
      alert('failed');
    }
  });
}

/** Prompt plugin */
Popup.registerPlugin('promptTemplate', function (buttontext, defaultValue, callback) {
    let promptValue = null;
    let promptChange = function (value) {
        promptValue = value;
    };

    this.create({
        title: "Select Template",
        content: <PromptTemplate onChange={promptChange} value={defaultValue} />,
        buttons: {
            left: ['cancel'],
            right: [{
                text: buttontext,
                className: 'success',
                action: function () {
                    callback(promptValue);
                    Popup.close();
                }

            }]
        }
    });
});

/** Prompt plugin */
Popup.registerPlugin('prompt', function (buttontext, title, defaultValue, placeholder, callback) {
    let promptValue = null;
    let promptChange = function (value) {
        promptValue = value;
    };

    this.create({
        title: title,
        content: <Prompt onChange={promptChange} placeholder={placeholder} value={defaultValue} />,
        buttons: {
            left: ['cancel'],
            right: [{
                text: buttontext,
                className: 'success',
                action: function () {
                    callback(promptValue);
                    Popup.close();
                }
            }]
        }
    });
});
