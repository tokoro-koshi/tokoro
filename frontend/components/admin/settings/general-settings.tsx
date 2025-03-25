'use client';

import type React from 'react';

import { useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import toast from 'react-hot-toast';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Separator } from '@/components/ui/separator';

interface GeneralSettingsProps {
  settings: {
    siteName: string;
    siteDescription: string;
    logo: string;
    favicon: string;
    primaryColor: string;
    itemsPerPage: number;
  };
}

export default function GeneralSettings({ settings }: GeneralSettingsProps) {
  const [formData, setFormData] = useState(settings);

  const updateSettingsMutation = useMutation({
    mutationFn: async (data: typeof formData) => {
      // This would be implemented using your settings API
      // For now, just simulating a successful update
      await new Promise((resolve) => setTimeout(resolve, 1000));
      return data;
    },
    onSuccess: () => {
      toast.success('Settings updated successfully');
    },
    onError: () => {
      toast.error('Failed to update settings');
    },
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === 'itemsPerPage' ? Number.parseInt(value, 10) : value,
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
            <Label htmlFor='siteName'>Site Name</Label>
            <Input
              id='siteName'
              name='siteName'
              value={formData.siteName}
              onChange={handleChange}
            />
          </div>
          <div className='space-y-2'>
            <Label htmlFor='siteDescription'>Site Description</Label>
            <Input
              id='siteDescription'
              name='siteDescription'
              value={formData.siteDescription}
              onChange={handleChange}
            />
          </div>
        </div>

        <div className='grid grid-cols-1 gap-4 md:grid-cols-2'>
          <div className='space-y-2'>
            <Label htmlFor='logo'>Logo URL</Label>
            <Input
              id='logo'
              name='logo'
              value={formData.logo}
              onChange={handleChange}
            />
          </div>
          <div className='space-y-2'>
            <Label htmlFor='favicon'>Favicon URL</Label>
            <Input
              id='favicon'
              name='favicon'
              value={formData.favicon}
              onChange={handleChange}
            />
          </div>
        </div>

        <div className='grid grid-cols-1 gap-4 md:grid-cols-2'>
          <div className='space-y-2'>
            <Label htmlFor='primaryColor'>Primary Color</Label>
            <div className='flex gap-2'>
              <Input
                id='primaryColor'
                name='primaryColor'
                value={formData.primaryColor}
                onChange={handleChange}
              />
              <div
                className='h-10 w-10 rounded border'
                style={{ backgroundColor: formData.primaryColor }}
              />
            </div>
          </div>
          <div className='space-y-2'>
            <Label htmlFor='itemsPerPage'>Items Per Page</Label>
            <Input
              id='itemsPerPage'
              name='itemsPerPage'
              type='number'
              min='5'
              max='100'
              value={formData.itemsPerPage}
              onChange={handleChange}
            />
          </div>
        </div>
      </div>

      <Separator />

      <Button type='submit' disabled={updateSettingsMutation.isPending}>
        {updateSettingsMutation.isPending ? 'Saving...' : 'Save Changes'}
      </Button>
    </form>
  );
}
