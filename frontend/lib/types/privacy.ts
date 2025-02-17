export interface ContactUs {
  socialLinks: string[];
  phoneNumber: string;
}

export interface Privacy {
  id: string;
  title: string;
  effectiveDate: string;
  lastUpdated: string;
  introduction: string;
  informationWeCollect: Record<string, string>;
  howWeUseCollectedInformation: Record<string, string>;
  howWeShareUserInformation: string;
  howWeProtectUserInformation: string;
  yourRights: string;
  contactUs: ContactUs;
}
