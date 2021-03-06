import React from 'react'
import Menu from 'common-components/lib/components/menu'
import { loadTemplates, loadTemplate } from 'common-components/lib/js/templateController.js';
import { getRootKarafUrl } from 'common-components/lib/js/common';
import { DialogMessage, DialogLoader, DialogOptions } from 'common-components/lib/components/dialogs'
import { searchComplex } from 'common-components/lib/js/searchController.js';

import Paper from '@material-ui/core/Paper';

import FilterCreator from './filtercreator.js'
import {SearchResults} from './results.js'

var karafRootAddr = getRootKarafUrl();

export class App extends React.Component {

  constructor(props) {
    super(props);

    this.handleTemplateLoad = this.handleTemplateLoad.bind(this);
    this.handleError = this.handleError.bind(this);
    this.handleUpdate = this.handleUpdate.bind(this);
    this.createQuery = this.createQuery.bind(this);
    this.handleSearch = this.handleSearch.bind(this);

    this.state = {
      cache: [],
      key: null,
      results: null,
      properties: null,
      query: this.createQuery()
    }
  }

  createQuery(name) {

    if (!name) {
      name = this.state && this.state.key ? this.state.cache[this.state.key].name : '' ;
    }

    return {
        operator: 'AND',
        criteria: [
          {
            key: 0,
            recordset: 'CoalesceEntity',
            field: 'name',
            operator: 'EqualTo',
            value: name,
            matchCase: false
          }
        ],
        groups: []
       }
     }

  componentDidMount() {

    var that = this;

    fetch(karafRootAddr + '/templates/998b040b-2c39-4c98-9a9d-61d565b46e28/recordsets/CoalesceEntity/fields')
      .then(res => res.json())
      .then(definition => {

        const { cache } = this.state;

        var recordsets = [];
        recordsets.push({name: 'CoalesceEntity', definition: definition});

        cache['CoalesceEntity'] = {
          recordsets: recordsets,
          name: ''
        };

        that.setState({
          key: 'CoalesceEntity',
          cache: cache,
          }
        );

    }).catch(function(error) {
      that.handleError("Loading Common Fields: " + error);
    });

    loadTemplates().then((templates) => {
      that.setState({
        templates: templates,
      })
    }).catch(function(error) {
      that.handleError("Loading Templates: " + error);
    });
  }

  handleError(message) {
    this.setState(() => {return {
      error: message,
      loading: null,
      promptTemplate: false
    }});
  }

  handleUpdate(data) {
    this.setState({query: data});
  }

  handleTemplateLoad(key) {

    const that = this;
    const { cache } = this.state;

    if (cache[key] == null)
    {
      console.log("Loading Template: " + key);

      loadTemplate(key).then(template => {

        var recordsets = [].concat(cache['CoalesceEntity'].recordsets);

        // Get Other Recordsets
        template.sectionsAsList.forEach(function(section) {
          recordsets = recordsets.concat(getRecordsets(section));
        });

        cache[key] = {
          recordsets: recordsets,
          name: template.name
        };

        console.log("APP Loading Template", recordsets);

        that.setState({
          key: key,
          query: this.createQuery(cache[key].name),
          promptTemplate: false
          }
        );

        }).catch((err) => {
          that.handleError(`Failed Loading Template: ${key}`);
        })
    } else {
      console.log("Loading Template (Cached): " + key);

      that.setState({
        key: key,
        promptTemplate: false
        });
    }
  }

  render() {

    const { cache, key, results, properties, query } = this.state;
   // console.log("App creating new filter creator", cache, key);
    return (
        <div>
        <Menu logoSrc={this.props.pjson.icon} title={this.props.pjson.title} items={[
          {
            id: 'search',
            name: 'Search',
            img: "/images/svg/search.svg",
            title: 'Execute Search',
            onClick: this.handleSearch
          },
          {
            id: 'select',
            name: 'Select',
            img: "/images/svg/template.svg",
            title: 'Select Template',
            onClick: () => {
              this.setState({promptTemplate: true})
            }
          }, {
            id: 'load',
            name: 'Load',
            img: "/images/svg/load.svg",
            title: 'Load Saved Criteria Selection',
            onClick: () => {
              this.handleError("(Comming Soon!!!) This will allow you to load previously saved criteria.");
            }
          }, {
            id: 'save',
            name: 'Save',
            img: "/images/svg/save.svg",
            title: 'Save Criteria Selection',
            onClick: () => {
              this.handleError("(Comming Soon!!!) This will allow you to save criteria.");
            }
          }, {
            id: 'reset',
            name: 'Reset',
            img: "/images/svg/erase.svg",
            title: 'Reset Criteria',
            onClick: () => {
              this.setState({query: this.createQuery()})
              console.log("Reset Criteria");
            }
          }
        ]}/>
          <div  style={{padding: '5px', margin: '10px'}}>
            { cache[key] != null &&
              <FilterCreator
                label={this.state.key ? this.state.cache[this.state.key].name : undefined}
                maxRows={10}
                recordsets={cache[key].recordsets}
                data={query}
                handleError={this.handleError}
                handleUpdate={this.handleUpdate}
              />

            }
            { results != null &&
              <Paper>
                <SearchResults
                  data={results}
                  properties={properties}
                  url={this.props.karafRootAddr}
                />
              </Paper>
            }
            { this.state.error &&
              <DialogMessage
                title="Error"
                opened={true}
                message={this.state.error}
                onClose={() => {this.setState({error: null})}}
              />
            }
            { this.state.loading  &&
              <DialogLoader
                title={this.state.loading}
                opened={true}
              />
            }
            { this.state.promptTemplate && this.state.templates &&
              <DialogOptions
                title="Select Template"
                open={true}
                onClose={() => {this.setState({promptTemplate: false})}}
                onClick={this.handleTemplateLoad}
                options={this.state.templates}
              />
            }
          </div>
        </div>
    )
  }

  handleSearch() {

    // Create Query
    var query = {
      "pageSize": 200,
      "pageNumber": 1,
      "propertyNames": [],
      "group": this.state.query
    };
    console.log("Index search", this.state.query);
    // Get additional columns
    // TODO Handle nested properties
    this.state.query.criteria.forEach(function (criteria) {
      query.propertyNames.push(criteria.recordset + "." + criteria.field);
      console.log("Index search", query.propertyNames);
    });

    // Display Spinner
    this.setState(() => {return {
      loading: "Searching..."
    }})

    // Submit Query
    searchComplex(query).then(response => {
      this.setState(() => {return {
        results: response,
        properties: query.propertyNames,
        loading: null
      }})
    }).catch(function(error) {
        this.handleError("Executing Search: " + error);
    });
  }
}

function getRecordsets(section) {

  var results = [];

  section.sectionsAsList.forEach(function(section) {
    results = results.concat(getRecordsets(section));
  });

  // Render Recordsets
  section.recordsetsAsList.forEach(function(recordset) {
    results.push({name: recordset.name, definition: recordset.fieldDefinitions});
  });

  return results;
}
