import { useEffect, useState } from "react";
import api from "../api/client";
import { DataTable } from "../components/DataTable";
import { LoadingState } from "../components/LoadingState";
import { User } from "../types";

export function UserManagementPage() {
  const [users, setUsers] = useState<User[] | null>(null);

  useEffect(() => {
    api.get<User[]>("/api/users").then((response) => setUsers(response.data));
  }, []);

  if (!users) {
    return <LoadingState label="Loading users..." />;
  }

  return (
    <DataTable
      title="User Management"
      rows={users}
      emptyMessage="No users found."
      columns={[
        { key: "name", header: "Full Name", render: (row) => row.fullName },
        { key: "username", header: "Username", render: (row) => row.username },
        { key: "email", header: "Email", render: (row) => row.email },
        { key: "roles", header: "Roles", render: (row) => row.roles.join(", ") },
        { key: "status", header: "Status", render: (row) => (row.active ? "Active" : "Disabled") }
      ]}
    />
  );
}
