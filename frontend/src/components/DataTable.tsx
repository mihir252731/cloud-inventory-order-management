import React from "react";

interface Column<T> {
  key: string;
  header: string;
  render: (row: T) => React.ReactNode;
}

export function DataTable<T>({
  title,
  columns,
  rows,
  emptyMessage
}: {
  title: string;
  columns: Column<T>[];
  rows: T[];
  emptyMessage: string;
}) {
  return (
    <div className="panel overflow-hidden">
      <div className="border-b border-slate-200 px-6 py-4">
        <h3 className="text-lg font-semibold text-slate-900">{title}</h3>
      </div>
      <div className="overflow-x-auto">
        <table className="min-w-full divide-y divide-slate-200 text-sm">
          <thead className="bg-slate-50">
            <tr>
              {columns.map((column) => (
                <th key={column.key} className="px-6 py-3 text-left font-semibold text-slate-600">
                  {column.header}
                </th>
              ))}
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100 bg-white">
            {rows.length === 0 ? (
              <tr>
                <td colSpan={columns.length} className="px-6 py-6 text-center text-slate-500">
                  {emptyMessage}
                </td>
              </tr>
            ) : (
              rows.map((row, index) => (
                <tr key={index}>
                  {columns.map((column) => (
                    <td key={column.key} className="px-6 py-4 text-slate-700">
                      {column.render(row)}
                    </td>
                  ))}
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
