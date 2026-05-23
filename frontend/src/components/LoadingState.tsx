export function LoadingState({ label = "Loading..." }: { label?: string }) {
  return (
    <div className="panel flex min-h-48 items-center justify-center p-8 text-sm text-slate-500">
      {label}
    </div>
  );
}
