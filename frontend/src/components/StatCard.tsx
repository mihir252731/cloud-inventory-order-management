export function StatCard({
  title,
  value,
  accent
}: {
  title: string;
  value: string | number;
  accent: string;
}) {
  return (
    <div className="panel p-5">
      <div className={`mb-3 h-2 w-12 rounded-full ${accent}`} />
      <p className="text-sm uppercase tracking-[0.2em] text-slate-500">{title}</p>
      <p className="mt-3 text-3xl font-semibold text-slate-900">{value}</p>
    </div>
  );
}
