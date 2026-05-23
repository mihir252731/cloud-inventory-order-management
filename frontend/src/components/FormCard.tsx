import type { ReactNode } from "react";

export function FormCard({
  title,
  children
}: {
  title: string;
  children: ReactNode;
}) {
  return (
    <div className="panel p-6">
      <h3 className="mb-4 text-lg font-semibold text-slate-900">{title}</h3>
      <div className="space-y-4">{children}</div>
    </div>
  );
}
