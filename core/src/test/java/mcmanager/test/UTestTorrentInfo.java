package mcmanager.test;

import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import mcmanager.exception.CoreException;
import mcmanager.utils.ApplicationUtils;
import mcmanager.utils.TorrentInfo;

import org.junit.Test;

public class UTestTorrentInfo {

    private final String testPath = ApplicationUtils.getApplicationHome() + 
            File.separator + "data" + File.separator;

    @Test
    public void testGetInfoT3363056() throws CoreException {
        TorrentInfo info = new TorrentInfo(new File(testPath + "[rutracker.org].t3363056.torrent"));
        Set<String> expected = new HashSet<String>();
        expected.add("Крылья, ноги и хвосты.avi");
        assertTrue(info.getInfo().equals(expected));
    }

    @Test
    public void testGetInfoT3725339() throws CoreException {
        TorrentInfo info = new TorrentInfo(new File(testPath + "[rutracker.org].t3725339.torrent"));
        Set<String> expected = new HashSet<String>();
        expected.add("Eternal Chaos - Dark God Of The Eternal (2011)/cover.jpg");
        expected.add("Eternal Chaos - Dark God Of The Eternal (2011)/07 - Rise Blasphemer.mp3");
        expected.add("Eternal Chaos - Dark God Of The Eternal (2011)/01 - Death's Overture.mp3");
        expected.add("Eternal Chaos - Dark God Of The Eternal (2011)/04 - Throne Of Hate.mp3");
        expected.add("Eternal Chaos - Dark God Of The Eternal (2011)/03 - The Black Flame Spirit.mp3");
        expected.add("Eternal Chaos - Dark God Of The Eternal (2011)/02 - Lord Of Chaos.mp3");
        expected.add("Eternal Chaos - Dark God Of The Eternal (2011)/09 - Through The Shadows Of Death.mp3");
        expected.add("Eternal Chaos - Dark God Of The Eternal (2011)/05 - Pentagram.mp3");
        expected.add("Eternal Chaos - Dark God Of The Eternal (2011)/08 - Dark God Of The Eternal.mp3");
        expected.add("Eternal Chaos - Dark God Of The Eternal (2011)/10 - Outro.mp3");
        expected.add("Eternal Chaos - Dark God Of The Eternal (2011)/06 - Serpent's Empire.mp3");
        assertTrue(info.getInfo().equals(expected));
    }

    @Test
    public void testGetInfoT3732061() throws CoreException {
        TorrentInfo info = new TorrentInfo(new File(testPath + "[rutracker.org].t3732061.torrent"));
        Set<String> expected = new HashSet<String>();
        expected.add("VMware Workstation 8.0 build 471780 for Linux/sha1.sum"); 
        expected.add("VMware Workstation 8.0 build 471780 for Linux/VMware-Workstation-Full-8.0.0-471780.i386.bundle");
        expected.add("VMware Workstation 8.0 build 471780 for Linux/serial.txt");
        expected.add("VMware Workstation 8.0 build 471780 for Linux/VMware-Workstation-Full-8.0.0-471780.x86_64.bundle");
        assertTrue(info.getInfo().equals(expected));
    }

    @Test
    public void testGetInfoT3469402() throws CoreException {
        TorrentInfo info = new TorrentInfo(new File(testPath + "[rutracker.org].t3469402.torrent"));
        Set<String> expected = new HashSet<String>();
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/GANT/WinRAR_Gant!_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Tango/WinRAR_Tango_32x32.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/AquaWorld/AquaWorld_128x128.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Delato/Delato_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Gorilla/WinRAR_Gorilla_64x64.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/AquaWorld/AquaWorld_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/OS X/WinRAR_OS_X_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/RARaddin/RARaddin_64x64.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Buuf/WinRAR_Buuf_48x48.v1_1_by_pkuwyc.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Buuf/WinRAR_Buuf_96x96.v1_1_by_pkuwyc.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Cryo64/WinRAR_Cryo.64x64.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Gorilla/WinRAR_Gorilla_32x32.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/XP/WinRAR_XP_64x64.1_02.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Crystal Clear/WinRAR_Crystal_Clear_32x32.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/AquaWorld/AquaWorld_64x64.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/JOM 2/WinRAR_JOM2-32x32.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Jr/WinRAR_Jr_48x48.1_01.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Alpha Dista/WinRAR_AlphaDista_64x64.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Windows 7/Windows7_WinRar_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Lord Darksys/LordDarksys.48x48.v101.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Noia Bogart/WinRAR_Noia_Bogart_64x64.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Roger's/Roger_s_WinRAR_Theme_48x48_2.1.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Senyum's/WinRAR_Senyum_s_32x32.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/GANT/WinRAR_Gant!_32x32.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Alpha Dista/WinRAR_AlphaDista_128x128.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Azeri-XP/WinRAR_Azeri-XP_64x64.1_01.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Noia Bogart/WinRAR_Noia_Bogart_16x16.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Tin/WinRAR_Tin_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Smile/WinRAR_Smile_d_32x32.1_00.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/JOM 2/WinRAR_JOM2-64x64.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/RefreshCL/WinRAR_RefreshCL_48x48.v1_1_by_pkuwyc.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Alpha Dista/WinRAR_AlphaDista_96x96.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Cartoon/WinRAR_Cartoon_64x64.v1_0_by_pkuwyc.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Crystal/WinRAR_Crystal_Theme_64x64.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Cartoon/WinRAR_Cartoon_32x32.v1_0_by_pkuwyc.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/XP/WinRAR_XP_48x48.1_02.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Vista Ultimate Revamped/Vista_Ultimate_96x96.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Cartoon/WinRAR_Cartoon_80x80.v1_0_by_pkuwyc.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Windows 3.1/Win3_1_32x32.v1_2.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/2.8.2.9/WinRAR_2.8.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Vista 2.0/WinRAR_for_Vista_32x.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Orbital/WinRAR_Orbital_64x64.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Imagination 3/WinRAR_Imagination3_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/RefreshCL/WinRAR_RefreshCL_80x80.v1_1_by_pkuwyc.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Last Order/WinRAR_LastOrder_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Noia Bogart/WinRAR_Noia_Bogart_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Professional/WinRAR_Professional.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Alpha Dista/WinRAR_AlphaDista_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Bucket/WinRAR_Bucket_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/AS-Kristal/AS-Kristal-32.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Lord Darksys/LordDarksys_32x32.v101.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Medieval/Medieval_32x32.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Stalk-Lab/Stalk-Lab_39x39.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/JOM/WinRAR_JOM_64x64.1_0.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/AS-Kristal/AS-Kristal-48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/GANT/WinRAR_Gant!_16x16.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Azeri-Yazi/WinRAR_Azeri-Yazi_48x48.1_70.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Vista 2.0/WinRAR_for_Vista_64x.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Imagination V.II/WinRAR_Imagination_VII_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Delato/Delato_32x32.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Azeri-Yazi/WinRAR_Azeri-Yazi_64x64.1_70.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Tulliana/Tulliana48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/MaRado/MaRado_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Orbital/WinRAR_Orbital_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Roger's/Roger_s_WinRAR_Theme_32x32_2.0.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Alpha Dista/WinRAR_AlphaDista_32x32.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Tango/WinRAR_Tango_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Buuf/WinRAR_Buuf_80x80.v1_1_by_pkuwyc.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Orbital/WinRAR_Orbital_32x32.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Orange-Gold/Winrar_Orange-Gold_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Vista Ultimate Revamped/Vista_Ultimate_128x128.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Alpha Dista/WinRAR_AlphaDista_80x80.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Crystal Clear/WinRAR_Crystal_Clear_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/OS X/WinRAR_OS_X_32x32.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Jr/WinRAR_Jr_64x64.1_01.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/GnomeRAR/GnomeRAR_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Vista 1.0/WinRAR_Vista_48x48.v1_0_by_pkuwyc.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Vista/Vista_WinRAR_48x48_1.1.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Lord Darksys/LordDarksys_16x16.v101.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/WinRAR.4.00.x64.exe");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Cartoon/WinRAR_Cartoon_48x48.v1_0_by_pkuwyc.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/India/WinRAR_India_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Vista 1.0/WinRAR_Vista_64x64.v1_0_by_pkuwyc.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Ka'ita-Hitam/Ka_ita-Hitam48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/AS-Kristal/AS-Kristal-64.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Vista Ultimate Revamped/Vista_Ultimate_64x64.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/FatCow/FatCow_32x32.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/aRRis/WinRAR_aRRis_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/OS X/WinRAR_OS_X_64x64.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Buuf/WinRAR_Buuf_64x64.v1_1_by_pkuwyc.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Smile/WinRAR_Smile_l_32x32.1_00.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/X-vision/WinRar_X-vision_32x32.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Windows 7 v2/Windows7_v2_WinRar_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Smile/WinRAR_Smile_d_48x48.1_00.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/AquaWorld/AquaWorld_32x32.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/JOM/WinRAR_JOM_32x32.1_0.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/XP/WinRAR_XP_32x32.1_02.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Crystal Clear/WinRAR_Crystal_Clear_64x64.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Noia Bogart/WinRAR_Noia_Bogart_32x32.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Money/WinRAR_Money 48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/JOM 2/WinRAR_JOM2-48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/CodeHammer v4i/CodeHammer.v4i.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Vista Mx/WinRAR_Vista_Mx_32x32.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Gorilla/WinRAR_Gorilla_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Reality/Reality_of_WinRAR_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Medieval/Medieval_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Vista Mx/WinRAR_Vista_Mx_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/JOM/WinRAR_JOM_48x48.1_0.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/PC.DE/WinRAR_PCDE_48x48.1.00.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Vista 1.0/WinRAR_Vista_80x80.v1_0_by_pkuwyc.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Vista 2.0/WinRAR_for_Vista_48x.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/MaRado/MaRado_32x32.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Reborn/WinRAR_Reborn_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Jr/WinRAR_Jr_32x32.1_01.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/RefreshCL/WinRAR_RefreshCL_32x32.v1_1_by_pkuwyc.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Stalk-Lab/Stalk-Lab_32x32.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/GANT/WinRAR_Gant!_64x64.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/WinRAR.4.00.x32.exe");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/RARaddin/RARaddin_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Vista 1.0/WinRAR_Vista_32x32.v1_0_by_pkuwyc.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/RefreshCL/WinRAR_RefreshCL_64x64.v1_1_by_pkuwyc.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Ka'ita-Hitam/Ka_ita-Hitam32x32.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Smile/WinRAR_Smile_l_48x48.1_00.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Vista Ultimate Revamped/Vista_Ultimate_80x80.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/MaRado/MaRado_64x64.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Buuf/WinRAR_Buuf_32x32.v1_1_by_pkuwyc.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Vista Ultimate Revamped/Vista_Ultimate_48x48.theme.rar");
        expected.add("WinRAR v.4.00 Final Silent installation (x32x64RUS) & Themes/Theme/Senyum's/WinRAR_Senyum_s_48x48.theme.rar");
        assertTrue(info.getInfo().equals(expected));
    }

}
