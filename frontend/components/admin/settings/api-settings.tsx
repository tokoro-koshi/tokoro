'use client';

import type React from 'react';

import { useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Switch } from '@/components/ui/switch';
import { Separator } from '@/components/ui/separator';
import toast from 'react-hot-toast';

interface ApiSettingsProps {
  settings: {
    rateLimit: number;
    timeout: number;
    enableCaching: boolean;
    cacheDuration: number;
  };
}

export default function ApiSettings({ settings }: ApiSettingsProps) {
  const [formData, setFormData] = useState(settings);

  const updateSettingsMutation = useMutation({
    mutationFn: async (data: typeof formData) => {
      // This would be implemented using your settings API
      await new Promise((resolve) => setTimeout(resolve, 1000)); // Mock API call
      return data;
    },
    onSuccess: () => {
      toast.success('Your API settings have been updated successfully.');
    },
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: ['rateLimit', 'timeout', 'cacheDuration'].includes(name)
        ? Number.parseInt(value, 10)
        : value,
    }));
  };

  const handleSwitchChange = (checked: boolean) => {
    setFormData((prev) => ({
      ...prev,
      enableCaching: checked,
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    updateSettingsMutation.mutate(formData);
  };

  return (
    <form onSubmit={handleSubmit} className='space-y-6'>
      <div className='space-y-4'>
        <div className='grid grid-cols-1 gap-4 md:grid-cols-2'>
          <div className='space-y-2'>
            <Label htmlFor='rateLimit'>Rate Limit (requests per minute)</Label>
            <Input
              id='rateLimit'
              name='rateLimit'
              type='number'
              min='10'
              max='1000'
              value={formData.rateLimit}
              onChange={handleChange}
            />
          </div>
          <div className='space-y-2'>
            <Label htmlFor='timeout'>Timeout (seconds)</Label>
            <Input
              id='timeout'
              name='timeout'
              type='number'
              min='5'
              max='120'
              value={formData.timeout}
              onChange={handleChange}
            />
          </div>
        </div>

        <div className='space-y-2'>
          <div className='flex items-center justify-between'>
            <Label htmlFor='enableCaching'>Enable Caching</Label>
            <Switch
              id='enableCaching'
              checked={formData.enableCaching}
              onCheckedChange={handleSwitchChange}
            />
          </div>
          <p className='text-sm text-muted-foreground'>
            Enable caching to improve API performance.
          </p>
        </div>

        {formData.enableCaching && (
          <div className='space-y-2'>
            <Label htmlFor='cacheDuration'>Cache Duration (seconds)</Label>
            <Input
              id='cacheDuration'
              name='cacheDuration'
              type='number'
              min='60'
              max='86400'
              value={formData.cacheDuration}
              onChange={handleChange}
            />
          </div>
        )}
      </div>

      <Separator />

      <div className='space-y-4'>
        <div>
          <h3 className='text-lg font-medium'>API Keys</h3>
          <p className='text-sm text-muted-foreground'>
            Manage your API keys for external integrations.
          </p>
        </div>

        <div className='rounded-md border bg-muted/50 p-4'>
          <p className='text-sm font-medium'>Your API Key</p>
          <div className='mt-2 flex'>
            <Input
              value='sk_live_51NzQRtGjksO8gzRMCOLlwQqG...'
              readOnly
              type='password'
            />
            <Button variant='outline' className='ml-2'>
              Reveal
            </Button>
            <Button variant='outline' className='ml-2'>
              Copy
            </Button>
          </div>
          <p className='mt-2 text-xs text-muted-foreground'>
            Last used: 2 hours ago
          </p>
        </div>

        <Button variant='outline'>Generate New API Key</Button>
      </div>

      <Separator />

      <Button type='submit' disabled={updateSettingsMutation.isPending}>
        {updateSettingsMutation.isPending ? 'Saving...' : 'Save Changes'}
      </Button>
    </form>
  );
}
