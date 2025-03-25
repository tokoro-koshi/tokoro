'use client';

import {
  Bar,
  BarChart,
  ResponsiveContainer,
  XAxis,
  YAxis,
  Tooltip,
} from 'recharts';

const data = [
  { name: 'Jan', users: 400, collections: 240 },
  { name: 'Feb', users: 300, collections: 139 },
  { name: 'Mar', users: 200, collections: 980 },
  { name: 'Apr', users: 278, collections: 390 },
  { name: 'May', users: 189, collections: 480 },
  { name: 'Jun', users: 239, collections: 380 },
  { name: 'Jul', users: 349, collections: 430 },
];

export default function ActivityChart() {
  return (
    <ResponsiveContainer width='100%' height={350}>
      <BarChart data={data}>
        <XAxis
          dataKey='name'
          stroke='#888888'
          fontSize={12}
          tickLine={false}
          axisLine={false}
        />
        <YAxis
          stroke='#888888'
          fontSize={12}
          tickLine={false}
          axisLine={false}
          tickFormatter={(value) => value.toString()}
        />
        <Tooltip
          contentStyle={{
            backgroundColor: '#ffffff',
            borderColor: '#e5e7eb',
          }}
        />
        <Bar
          dataKey='users'
          fill='currentColor'
          radius={[4, 4, 0, 0]}
          className='fill-primary'
        />
        <Bar
          dataKey='collections'
          fill='currentColor'
          radius={[4, 4, 0, 0]}
          className='fill-primary/50'
        />
      </BarChart>
    </ResponsiveContainer>
  );
}
