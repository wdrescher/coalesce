import React from 'react';
import ReactTable from 'react-table'

export class SearchResults extends React.Component {

  render() {

    const {data, properties} = this.props;

    var hits = data.hits;

    // Always show entity key
    var columns = [
      {
        Header: 'Key',
        accessor: 'entityKey'
      }
    ];

    // Add additional columns
    properties.forEach(function (property) {
      var parts = property.split(".");

      columns.push({
        Header: parts[1],
        accessor: parts[1]
      })
    });

    // {this.openEditor.bind(this, cell.row.entityKey)}

    // Add button for viewing entity
    columns.push({
      Header: '',
      accessor: 'button',
      Cell: (cell) => (
        <button className="form-control" title="Delete" onClick={() => window.open(this.props.url + "/entityeditor/?entitykey=" + cell.row.entityKey, '_blank')}>
          View
        </button>
      )
    });

    var tabledata;

    if (hits != null) {

      tabledata = hits;

      // Add additional column data
      tabledata.forEach(function (hit) {
        for (var ii=1; ii<columns.length - 1; ii++) {
            hit[columns[ii].accessor] = hit.values[ii-1];
        }
      });

    }

    return (
      <ReactTable
        data={tabledata}
        columns={columns}
      />
    )

  }


}

SearchResults.defaultProps = {
  url: 'http://' + window.location.hostname + ':8181',
  data: [],
  properties: []
}
